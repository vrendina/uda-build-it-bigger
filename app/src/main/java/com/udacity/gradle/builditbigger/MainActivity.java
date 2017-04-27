package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;

import com.udacity.gradle.builditbigger.backend.jokeApi.model.CloudJoke;
import com.udacity.gradle.joketeller.Joke;
import com.udacity.gradle.joketeller.JokeTeller;
import com.udacity.gradle.jokeviewer.JokeViewerActivity;

import java.io.IOException;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Joke>{

    public static final int TASK_LOCAL_ID = 100;
    public static final int TASK_REMOTE_ID = 200;

    // Number of times to retry if we get the same joke
    public static final int RETRY_LIMIT = 3;

    public static final String LAST_JOKE_KEY = "lastJoke";

    private TextView loadingTextView;
    private ProgressBar loadingProgressBar;
    private Button tellJokeButton;

    private Joke lastJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingTextView = (TextView) findViewById(R.id.tv_loading_joke);
        loadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_joke);
        tellJokeButton = (Button) findViewById(R.id.btn_tell_joke);

        getSupportLoaderManager().initLoader(TASK_LOCAL_ID, null, this);
        getSupportLoaderManager().initLoader(TASK_REMOTE_ID, null, this);

        if(savedInstanceState != null) {
            lastJoke = (Joke) savedInstanceState.getSerializable(LAST_JOKE_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the last joke we told so we can always get fresh material!
        if(lastJoke != null) {
            outState.putSerializable(LAST_JOKE_KEY, lastJoke);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleLoadingIndicator(boolean show) {
        if(show) {
            tellJokeButton.setEnabled(false);
            loadingTextView.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            tellJokeButton.setEnabled(true);
            loadingTextView.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    public void tellJoke(View view) {
        toggleLoadingIndicator(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String jokeSourceKey = prefs.getString(getString(R.string.sp_key_joke_source),
                getString(R.string.default_joke_source));

        int taskId = 0;
        if(jokeSourceKey.equals(getString(R.string.key_joke_source_local))) {
            taskId = TASK_LOCAL_ID;
        } else if(jokeSourceKey.equals(getString(R.string.key_joke_source_gce))) {
            taskId = TASK_REMOTE_ID;
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(taskId, null, this);
        loaderManager.getLoader(taskId).forceLoad();
    }

    private Joke loadLocalData() {
        // Artificially inflate the time it takes to get a local joke
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JokeTeller teller = new JokeTeller();
        return teller.tellJoke();
    }

    private Joke loadRemoteData() {
        // Artificially inflate the time it takes to get a "remote" joke
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl(getString(R.string.gce_api_server_address))
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });

        JokeApi jokeService = builder.build();

        try {
            CloudJoke cloudJoke = jokeService.tellJoke().execute();
            return Joke.create(cloudJoke.getSetup(), cloudJoke.getPunchLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Loader<Joke> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<Joke>(this) {
            @Override
            public Joke loadInBackground() {

                Joke joke = null;

                int retryCount = 0;

                switch (id) {
                    case TASK_LOCAL_ID:
                        Timber.d("Telling joke from local java library...");
                        joke = loadLocalData();

                        if(lastJoke != null) {
                            while (joke.equals(lastJoke) && retryCount < RETRY_LIMIT) {
                                Timber.d("Got the same joke, looking for fresh material!");
                                joke = loadLocalData();
                                retryCount++;
                            }
                        }

                        break;

                    case TASK_REMOTE_ID:
                        Timber.d("Telling joke from remote gce source...");
                        joke = loadRemoteData();

                        if(lastJoke != null) {
                            while (joke.equals(lastJoke) && retryCount < RETRY_LIMIT) {
                                Timber.d("Got the same joke, looking for fresh material!");
                                joke = loadRemoteData();
                                retryCount++;
                            }
                        }

                        break;
                }
                return joke;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Joke> loader, Joke data) {
        Timber.d("Got joke data -- " + data.toString());

        // If we haven't seen this joke yet, lets display it
        if(lastJoke == null || !lastJoke.equals(data)) {
            lastJoke = data;

            Intent jokeViewerIntent = new Intent(this, JokeViewerActivity.class);
            jokeViewerIntent.putExtra(JokeViewerActivity.JOKE_KEY, data);
            startActivity(jokeViewerIntent);
        }

        toggleLoadingIndicator(false);
    }

    @Override
    public void onLoaderReset(Loader<Joke> loader) {

    }
}
