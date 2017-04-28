package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

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
    public static final String LOADING_STATE_KEY = "loadingState";

    private TextView loadingTextView;
    private ProgressBar loadingProgressBar;
    private Button tellJokeButton;

    private Joke lastJoke;
    private boolean loadingState;

    private Joke cachedJoke;
    private boolean haveResult;

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
            loadingState = savedInstanceState.getBoolean(LOADING_STATE_KEY);
        }
        toggleLoadingIndicator(loadingState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Always save the loading state
        outState.putBoolean(LOADING_STATE_KEY, loadingState);

        // Save the last joke we told so we can always get fresh material!
        outState.putSerializable(LAST_JOKE_KEY, lastJoke);

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

    private void toggleLoadingIndicator(boolean visible) {
        loadingState = visible;
        if(visible) {
            tellJokeButton.setEnabled(false);
            loadingTextView.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            tellJokeButton.setEnabled(true);
            loadingTextView.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void showError() {
        Toast.makeText(this, R.string.error_loading_joke, Toast.LENGTH_LONG).show();
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
        Timber.d("Telling joke from local java library...");
        // Artificially inflate the time it takes to get a local joke
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JokeTeller teller = new JokeTeller();
        return teller.tellJoke();
    }

    private Joke loadRemoteData() {
        Timber.d("Telling joke from remote gce source...");
        // Artificially inflate the time it takes to get a "remote" joke
        try {
            Thread.sleep(1000);
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
                })
                .setApplicationName(getString(R.string.app_name));

        JokeApi jokeService = builder.build();

        try {
            CloudJoke cloudJoke = jokeService.tellJoke().execute();
            return Joke.create(cloudJoke.getSetup(), cloudJoke.getPunchLine());
        } catch (IOException e) {
            Timber.e("Could not load data from cloud service. Is the service running?");
        }

        return null;
    }

    @Override
    public Loader<Joke> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<Joke>(this) {

            private Joke loadJoke() {
                Joke joke = null;
                switch(id) {
                    case TASK_LOCAL_ID:
                        joke = loadLocalData();
                    break;
                    case TASK_REMOTE_ID:
                        joke = loadRemoteData();
                }
                return joke;
            }

            @Override
            public Joke loadInBackground() {
                int retryCount = 0;

                Joke joke = loadJoke();
                if(lastJoke != null && joke != null) {
                    while (joke.equals(lastJoke) && retryCount < RETRY_LIMIT) {
                        Timber.d("Got the same joke, looking for fresh material!");
                        joke = loadJoke();
                        retryCount++;
                    }
                }

                return joke;
            }

            @Override
            protected void onStartLoading() {
                if(haveResult) {
                    deliverResult(cachedJoke);
                }
            }

            @Override
            public void deliverResult(Joke data) {
                // This method is called even when the app is put into the background so we want to cache data here
                Timber.d("Called deliverResult");
                cachedJoke = data;
                haveResult = true;

                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Joke> loader, @Nullable Joke data) {
        // onLoadFinished is only called if the UI is visible!
        Timber.d("Called onLoadFinished");
        if(data == null) {
            showError();
        } else {
            Timber.d("Got joke data -- " + data.toString());
            // If we haven't seen this joke yet, lets display it
            if (lastJoke == null || !lastJoke.equals(data)) {

                lastJoke = data;

                Intent jokeViewerIntent = new Intent(this, JokeViewerActivity.class);
                jokeViewerIntent.putExtra(JokeViewerActivity.JOKE_KEY, data);
                startActivity(jokeViewerIntent);
            }
        }
        cachedJoke = null;
        haveResult = false;

        toggleLoadingIndicator(false);
    }

    @Override
    public void onLoaderReset(Loader<Joke> loader) {
    }
}
