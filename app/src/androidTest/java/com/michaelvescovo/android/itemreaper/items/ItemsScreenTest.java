package com.michaelvescovo.android.itemreaper.items;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.add_item.AddItemActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemsScreenTest {

    private static final String PACKAGE_NAME = "com.michaelvescovo.android.itemreaper.mock";

    @Rule
    public IntentsTestRule<ItemsActivity> mActivityRule = new IntentsTestRule<>(
            ItemsActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void addItemButtonVisible() {
        onView(withId(R.id.add_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAddItemButton_LaunchesAddItemUi() {
        onView(withId(R.id.add_item)).perform(click());
        intended(hasComponent(hasClassName(AddItemActivity.class.getName())));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }
}
