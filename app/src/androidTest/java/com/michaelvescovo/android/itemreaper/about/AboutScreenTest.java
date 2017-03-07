package com.michaelvescovo.android.itemreaper.about;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutScreenTest {

    @Rule
    public ActivityTestRule<AboutActivity> mActivityRule = new ActivityTestRule<>(
            AboutActivity.class);

    @Test
    public void titleVisible() {
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
    }

    @Test
    public void itemReaperTitleVisible() {
        onView(withText(R.string.title_item_reaper)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.title_item_reaper)).check(matches(isDisplayed()));
    }

    @Test
    public void openSourceStatementVisible() {
        onView(withText(R.string.open_source_statement)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.open_source_statement)).check(matches(isDisplayed()));
    }

    @Test
    public void forkStatementVisible() {
        onView(withText(R.string.fork_statement)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.fork_statement)).check(matches(isDisplayed()));
    }

    @Test
    public void attributionsTitleVisible() {
        onView(withText(R.string.title_attributions)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.title_attributions)).check(matches(isDisplayed()));
    }

    @Test
    public void reaperIconAttributionVisible() {
        onView(withText(R.string.reaper_icon_attribution)).check(matches(isDisplayed()));
        rotateScreen();
        onView(withText(R.string.reaper_icon_attribution)).check(matches(isDisplayed()));
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;
        Activity activity = mActivityRule.getActivity();
        activity.setRequestedOrientation(
                orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        );
    }
}
