package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.udacity.gradle.joketeller.JokeTeller;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private JokeTeller jokeTeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the local joke teller
        jokeTeller = new JokeTeller();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Timber.d("Loading settings activity...");

            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String jokeSourceKey = prefs.getString(getString(R.string.sp_key_joke_source),
                getString(R.string.default_joke_source));

        if(jokeSourceKey.equals(getString(R.string.key_joke_source_local))) {
            Timber.d("Telling joke from local java library...");
        } else if(jokeSourceKey.equals(getString(R.string.key_joke_source_gce))) {
            Timber.d("Telling joke from remote gce source...");
        }

        Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
    }


}
