package com.michaelvescovo.android.itemreaper.edit_item;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.items.ItemsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
@LargeTest
public class EditItemScreenTest {

    @Rule
    public IntentsTestRule<ItemsActivity> mActivityRule =
            new IntentsTestRule<ItemsActivity>(ItemsActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    ((ItemReaperApplication) InstrumentationRegistry.getTargetContext()
                            .getApplicationContext()).getRepositoryComponent()
                            .getRepository().deleteAllItems(USER_ID);
                }
            };

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                ITEM_2
        );
    }

    private Item mItem;

    public EditItemScreenTest(Item item) {
        mItem = item;
    }

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Before
    public void setup() {
        onView(withId(R.id.edit_item)).perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void titleVisible() {
        onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
    }

    @Test
    public void requiredTitleVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_required)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void categoryEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void typeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void expiryTitleTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void expiryDayEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_day)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void expiryMonthEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_month)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void expiryYearEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_year)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void optionalTitleVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_optional)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void mainColourEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_primary_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateTitleVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateDayEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_day)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateMonthEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_month)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateYearEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_year)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void pricePaidEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void discountEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void subCategoryEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subTypeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType2EditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType3EditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void primaryColourShadeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_primary_colour_shade)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void secondaryColourEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_secondary_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void sizeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void brandEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void shopEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void noteEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void takePhotoMenuOptionVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()));
    }

    @Test
    public void selectImageMenuOptionVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()));
    }

    @Test
    public void itemDoesNotHaveImage_ImageNotVisible() {
        closeSoftKeyboard();
        if (mItem.getImageUrl() == null) {
            try {
                onView(withId(R.id.edit_item_image)).perform(scrollTo())
                        .check(matches(not(isDisplayed())));
            } catch (PerformException ignore) {}
        }
    }

    @Test
    public void itemHasImage_ShowsImage() {
        closeSoftKeyboard();
        if (mItem.getImageUrl() != null) {
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }
}
