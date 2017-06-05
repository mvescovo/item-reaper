package com.michaelvescovo.android.itemreaper.item_details;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.EspressoHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemDetailsScreenTest {

    @Rule
    public IntentsTestRule<ItemDetailsActivity> mActivityRule =
            new IntentsTestRule<>(ItemDetailsActivity.class, true, false);

    private EspressoHelper mEspressoHelper;
    private Item mItem;

    @Before
    public void setup() {
        mItem = ITEM_1;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEM_ID, mItem.getId());
        mActivityRule.launchActivity(intent);
        mEspressoHelper = new EspressoHelper(
                InstrumentationRegistry.getTargetContext(), mActivityRule.getActivity());
    }

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void titleVisible() {
        onView(withId(R.id.appbar_title)).check(matches(isDisplayed()));
        mEspressoHelper.rotateScreen();
        onView(withId(R.id.appbar_title)).check(matches(isDisplayed()));
    }
}
