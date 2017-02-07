package com.michaelvescovo.android.itemreaper.about;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    public void attributionsTitleVisible() {
        onView(withId(R.id.title_attributions)).check(matches(isDisplayed()));
        onView(withId(R.id.title_attributions)).check(matches(withText(
                R.string.title_attributions)));
    }

    @Test
    public void reaperIconAttributionVisible() {
        onView(withId(R.id.reaper_icon_attribution)).check(matches(isDisplayed()));
        onView(withId(R.id.reaper_icon_attribution)).check(matches(withText(
                R.string.reaper_icon_attribution)));
    }

}
