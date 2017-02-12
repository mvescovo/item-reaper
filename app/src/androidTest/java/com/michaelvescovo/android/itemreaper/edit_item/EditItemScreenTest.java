package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditItemScreenTest {

    @Rule
    public IntentsTestRule<EditItemActivity> mActivityRule =
            new IntentsTestRule<EditItemActivity>(EditItemActivity.class);

    @Test
    public void saveMenuOptionVisible() {
        onView(withId(R.id.action_save)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSave_ShowsItemInList() {
        // Can't test this until the items feature is working
    }
}
