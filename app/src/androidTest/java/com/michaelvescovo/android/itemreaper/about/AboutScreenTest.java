package com.michaelvescovo.android.itemreaper.about;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoHelperMethods;

import org.junit.Before;
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

    private EspressoHelperMethods mEspressoHelperMethods;

    @Before
    public void setup() {
        mEspressoHelperMethods = new EspressoHelperMethods(InstrumentationRegistry.getTargetContext(),
                mActivityRule.getActivity());
    }

    @Test
    public void titleVisible() {
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
    }

    @Test
    public void itemReaperTitleVisible() {
        onView(withText(R.string.title_item_reaper)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.title_item_reaper)).check(matches(isDisplayed()));
    }

    @Test
    public void openSourceStatementVisible() {
        onView(withText(R.string.open_source_statement)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.open_source_statement)).check(matches(isDisplayed()));
    }

    @Test
    public void forkStatementVisible() {
        onView(withText(R.string.fork_statement)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.fork_statement)).check(matches(isDisplayed()));
    }

    @Test
    public void attributionsTitleVisible() {
        onView(withText(R.string.title_attributions)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.title_attributions)).check(matches(isDisplayed()));
    }

    @Test
    public void reaperIconAttributionVisible() {
        onView(withText(R.string.reaper_icon_attribution)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.reaper_icon_attribution)).check(matches(isDisplayed()));
    }
}
