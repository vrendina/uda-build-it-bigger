package com.udacity.gradle.builditbigger;

import com.udacity.gradle.joketeller.Joke;

public class MainActivity extends BaseMainActivity {
    @Override
    protected void showJoke(Joke joke) {
        // No need to display an advertisement first in the full version!
        launchJokeViewer(joke);
    }
}
