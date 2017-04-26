package com.udacity.gradle.jokeviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.udacity.gradle.joketeller.Joke;

public class JokeViewerActivity extends AppCompatActivity {

    public static final String JOKE_KEY = "jokeKey";

    private TextView jokeSetupTextView;
    private TextView jokePunchLineTextView;

    private Joke joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_viewer);

        jokeSetupTextView = (TextView) findViewById(R.id.tv_joke_setup);
        jokePunchLineTextView = (TextView) findViewById(R.id.tv_joke_punch_line);

        joke = (Joke) getIntent().getSerializableExtra(JOKE_KEY);

        if(savedInstanceState != null) {
            joke = (Joke)savedInstanceState.getSerializable(JOKE_KEY);
        }

        setupJoke();
    }

    private void setupJoke() {
        if(joke == null) {
            jokeSetupTextView.setText(R.string.joke_viewer_error);
            jokePunchLineTextView.setText(R.string.joke_viewer_error_punch_line);
        } else {
            jokeSetupTextView.setText(joke.setup);
            jokePunchLineTextView.setText(joke.punchLine);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(JOKE_KEY, joke);
        super.onSaveInstanceState(outState);
    }

    public void showPunchLine(View view) {
        jokePunchLineTextView.setVisibility(View.VISIBLE);
    }
}
