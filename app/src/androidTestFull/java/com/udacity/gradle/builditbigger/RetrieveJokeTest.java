package com.udacity.gradle.builditbigger;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RetrieveJokeTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void pressingTellJokeGetsJokeFromLocalLibrary() {
        // Make sure we are pulling data from the local library
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(context.getString(R.string.sp_key_joke_source), context.getString(R.string.key_joke_source_local));
        editor.commit();

        // Launch the activity
        activityTestRule.launchActivity(new Intent());

        // Click on the tell joke button
        onView(allOf(withId(R.id.btn_tell_joke), isDisplayed())).perform(click());

        // Check that the joke setup is displayed and is not empty
        onView(allOf(withId(R.id.tv_joke_setup), isDisplayed())).check(matches(not(withText(""))));

        // Click on the show punch line button
        onView(allOf(withId(R.id.btn_show_punch_line), isDisplayed())).perform(click());

        // Check if the punch line is visible
        onView(allOf(withId(R.id.tv_joke_punch_line), isDisplayed())).check(matches(not(withText(""))));
    }

    @Test
    public void pressingTellJokeGetsJokeFromCloudServer() {
        // Make sure we are pulling data from the cloud server
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(context.getString(R.string.sp_key_joke_source), context.getString(R.string.key_joke_source_gce));
        editor.commit();

        // Launch the activity
        activityTestRule.launchActivity(new Intent());

        // Click on the tell joke button
        onView(allOf(withId(R.id.btn_tell_joke), isDisplayed())).perform(click());

        // Check that the joke setup is displayed and is not empty
        onView(allOf(withId(R.id.tv_joke_setup), isDisplayed())).check(matches(not(withText(""))));

        // Click on the show punch line button
        onView(allOf(withId(R.id.btn_show_punch_line), isDisplayed())).perform(click());

        // Check if the punch line is visible
        onView(allOf(withId(R.id.tv_joke_punch_line), isDisplayed())).check(matches(not(withText(""))));
    }
}
