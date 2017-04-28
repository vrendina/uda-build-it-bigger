package com.udacity.gradle.jokeviewer;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.gradle.joketeller.Joke;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class JokeViewerActivityTest {

    private Joke testJoke;

    @Rule
    public ActivityTestRule<JokeViewerActivity> activityTestRule
            = new ActivityTestRule<>(JokeViewerActivity.class, false, false);

    @Before
    public void initializeTestData() {
        String setup = "How do you make a kleenex dance?";
        String punchLine = "Put a little boogie in it.";

        testJoke = Joke.create(setup, punchLine);

        Intent intent = new Intent();
        intent.putExtra(JokeViewerActivity.JOKE_KEY, testJoke);

        activityTestRule.launchActivity(intent);
    }

    @Test
    public void jokeSetupIsDisplayed() {
        onView(allOf(withId(R.id.tv_joke_setup), isDisplayed()))
                .check(matches(withText(testJoke.setup())));
    }

    @Test
    public void jokePunchLineIsDisplayedWhenButtonPressed() {
        onView(withId(R.id.btn_show_punch_line)).perform(click());

        onView(allOf(withId(R.id.tv_joke_punch_line), isDisplayed()))
                .check(matches(withText(testJoke.punchLine())));
    }
}
