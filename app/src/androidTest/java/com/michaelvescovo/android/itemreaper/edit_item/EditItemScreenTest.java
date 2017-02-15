package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.fail;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditItemScreenTest {

    @Rule
    public IntentsTestRule<EditItemActivity> mActivityRule =
            new IntentsTestRule<>(EditItemActivity.class);

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
    public void saveMenuOptionVisible() {
        onView(withId(R.id.action_save)).check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateEditTextVisible() {
        onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void pricePaidEditTextVisible() {
        onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void discountEditTextVisible() {
        onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void expiryEditTextVisible() {
        onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void categoryEditTextVisible() {
        onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subCategoryEditTextVisible() {
        onView(withId(R.id.edit_sub_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void typeEditTextVisible() {
        onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subTypeEditTextVisible() {
        onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType2EditTextVisible() {
        onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType3EditTextVisible() {
        onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void primaryColourEditTextVisible() {
        onView(withId(R.id.edit_primary_colour)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void primaryColourShadeEditTextVisible() {
        onView(withId(R.id.edit_primary_colour_shade)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void secondaryColourEditTextVisible() {
        onView(withId(R.id.edit_secondary_colour)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void sizeEditTextVisible() {
        onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void brandEditTextVisible() {
        onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void shopEditTextVisible() {
        onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionEditTextVisible() {
        onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void noteEditTextVisible() {
        onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void clickSave_ShowsAllItemDetailsInDetailView() {
        fail("Can't test this until the item details feature is working");
    }
}
