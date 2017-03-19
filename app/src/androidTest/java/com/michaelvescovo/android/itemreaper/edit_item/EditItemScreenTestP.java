package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.EspressoHelperMethods;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.edit_item.EditItemFragment.getPriceFromTotalCents;
import static com.michaelvescovo.android.itemreaper.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
@LargeTest
public class EditItemScreenTestP {

    @Rule
    public IntentsTestRule<EditItemActivity> mActivityRule =
            new IntentsTestRule<>(EditItemActivity.class, true, false);

    private EspressoHelperMethods mEspressoHelperMethods;
    private Item mItem;

    public EditItemScreenTestP(Item item) {
        mItem = item;
    }

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                ITEM_2
        );
    }

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(EditItemActivity.EXTRA_ITEM_ID, mItem.getId());
        mActivityRule.launchActivity(intent);
        mEspressoHelperMethods = new EspressoHelperMethods(
                InstrumentationRegistry.getTargetContext(), mActivityRule.getActivity());

        mEspressoHelperMethods.setPortrait();
        Espresso.closeSoftKeyboard();
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
    public void itemHasPurchaseDate_ShowsPurchaseDate() {
        if (mItem.getPurchaseDate() != -1) {
            Espresso.closeSoftKeyboard();
            Calendar purchaseDate = Calendar.getInstance();
            purchaseDate.setTimeInMillis(mItem.getPurchaseDate());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd/MMMM/YY", Locale.getDefault());
            String purchaseDateString = simpleDateFormat.format(purchaseDate.getTime());

            confirmDateCustomSelected(R.id.purchase_date_spinner, purchaseDateString);
//                mEspressoHelperMethods.rotateScreen();
//                confirmDateCustomSelected(R.id.purchase_date_spinner, purchaseDateString);
        }
    }

    @Test
    public void itemHasExpiry_ShowsExpiry() {
        if (mItem.getExpiry() != -1) {
            Espresso.closeSoftKeyboard();
            Calendar expiryDate = Calendar.getInstance();
            expiryDate.setTimeInMillis(mItem.getExpiry());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd/MMMM/YY", Locale.getDefault());
            String expiryDateString = simpleDateFormat.format(expiryDate.getTime());

            confirmDateCustomSelected(R.id.expiry_date_spinner, expiryDateString);
//                mEspressoHelperMethods.rotateScreen();
//                confirmDateCustomSelected(R.id.expiry_date_spinner, expiryDateString);
        }
    }

    @Test
    public void itemHasShop_ShowsShop() {
        if (mItem.getShop() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getShop())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getShop())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasPrice_ShowsPrice() {
        if (mItem.getPricePaid() != -1) {
            Espresso.closeSoftKeyboard();
            String priceString = getPriceFromTotalCents(mItem.getPricePaid());
            onView(withText(priceString)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(priceString)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasDiscount_ShowsDiscount() {
        if (mItem.getDiscount() != -1) {
            Espresso.closeSoftKeyboard();
            String discountString = getPriceFromTotalCents(mItem.getDiscount());
            onView(withText(discountString)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(discountString)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasCategory_ShowsCategory() {
        if (mItem.getCategory() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getCategory())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getCategory())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSubCategory_ShowsSubCategory() {
        if (mItem.getSubCategory() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubCategory())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubCategory())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasType_ShowsType() {
        if (mItem.getType() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getType())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getType())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSubType_ShowsSubType() {
        if (mItem.getSubtype() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSubType2_ShowsSubType2() {
        if (mItem.getSubtype2() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype2())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype2())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSubType3_ShowsSubType() {
        if (mItem.getSubtype3() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype3())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSubtype3())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasPrimaryColour_ShowsPrimaryColour() {
        if (mItem.getPrimaryColour() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getPrimaryColour())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getPrimaryColour())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasPrimaryColourShade_ShowsPrimaryColourShade() {
        if (mItem.getPrimaryColourShade() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getPrimaryColourShade())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getPrimaryColourShade())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSecondaryColour_ShowsSecondaryColour() {
        if (mItem.getSecondaryColour() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSecondaryColour())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSecondaryColour())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasSize_ShowsSize() {
        if (mItem.getSize() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSize())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getSize())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasBrand_ShowsBrand() {
        if (mItem.getBrand() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getBrand())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getBrand())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasDescription_ShowsDescription() {
        if (mItem.getDescription() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getDescription())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getDescription())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemHasNote_ShowsNote() {
        if (mItem.getNote() != null) {
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getNote())).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withText(mItem.getNote())).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void itemDoesNotHaveImage_ImageNotVisible() {
        Espresso.closeSoftKeyboard();
        if (mItem.getImageUrl() == null) {
            // Need to scroll in case view exists and is off screen.
            // If the view doesn't exist it will fail to scroll so we need a try catch
            try {
                onView(withId(R.id.edit_item_image)).perform(scrollTo())
                        .check(matches(not(isDisplayed())));
            } catch (PerformException ignore) {
            }
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            try {
                onView(withId(R.id.edit_item_image)).perform(scrollTo())
                        .check(matches(not(isDisplayed())));
            } catch (PerformException ignore) {
            }
        }
    }

    @Test
    public void itemHasImage_ShowsImage() {
        Espresso.closeSoftKeyboard();
        if (mItem.getImageUrl() != null) {
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));
            mEspressoHelperMethods.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));
        }
    }

    private static void confirmDateCustomSelected(int spinnerId, String customDateString) {
        onView(withId(spinnerId))
                .check(matches(withSpinnerText(containsString(customDateString))));
    }
}
