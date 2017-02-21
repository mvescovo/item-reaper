package com.michaelvescovo.android.itemreaper.edit_item;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
@LargeTest
public class EditItemScreenTest {

    @Rule
    public IntentsTestRule<EditItemActivity> mActivityRule =
            new IntentsTestRule<>(EditItemActivity.class, true, false);

    private Item mItem;

    public EditItemScreenTest(Item item) {
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
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Before
    public void setup() {
        Intent intent = new Intent();
        intent.putExtra(EditItemActivity.EXTRA_ITEM_ID, mItem.getId());
        mActivityRule.launchActivity(intent);
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
    public void itemHasCategory_ShowsCategory() {
        if (mItem.getCategory() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getCategory())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Category text not displayed.");
            }
        }
    }

    @Test
    public void typeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasType_ShowsType() {
        if (mItem.getType() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getType())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Type text not displayed.");
            }
        }
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
    public void itemHasExpiry_ShowsExpiry() {
        if (mItem.getExpiry() != -1) {
            closeSoftKeyboard();
            try {
                Calendar expiry = Calendar.getInstance();
                expiry.setTimeInMillis(mItem.getExpiry());
                int day = expiry.get(Calendar.DAY_OF_MONTH);
                int month = expiry.get(Calendar.MONTH);
                month++; // Months start at 0.
                int year = expiry.get(Calendar.YEAR);

                onView(withText(String.valueOf(day))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                onView(withText(String.valueOf(month))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                onView(withText(String.valueOf(year))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Expiry text not displayed.");
            }
        }
    }

    @Test
    public void optionalTitleVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_optional)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void primaryColourEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_primary_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemHasPrimaryColour_ShowsPrimaryColour() {
        if (mItem.getPrimaryColour() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getPrimaryColour())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Primary colour text not displayed.");
            }
        }
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
    public void itemHasPurchaseDate_ShowsPurchaseDate() {
        if (mItem.getPurchaseDate() != -1) {
            closeSoftKeyboard();
            try {
                Calendar purchaseDate = Calendar.getInstance();
                purchaseDate.setTimeInMillis(mItem.getPurchaseDate());
                int day = purchaseDate.get(Calendar.DAY_OF_MONTH);
                int month = purchaseDate.get(Calendar.MONTH);
                month++; // Months start at 0.
                int year = purchaseDate.get(Calendar.YEAR);

                onView(withText(String.valueOf(day))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                onView(withText(String.valueOf(month))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                onView(withText(String.valueOf(year))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("PurchaseDate text not displayed.");
            }
        }
    }

    @Test
    public void pricePaidEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasPrice_ShowsPrice() {
        if (mItem.getPricePaid() != -1) {
            closeSoftKeyboard();
            try {
                onView(withText(String.valueOf(mItem.getPricePaid()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Price text not displayed.");
            }
        }
    }

    @Test
    public void discountEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemHasDiscount_ShowsDiscount() {
        if (mItem.getDiscount() != -1) {
            closeSoftKeyboard();
            try {
                onView(withText(String.valueOf(mItem.getDiscount()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Discount text not displayed.");
            }
        }
    }

    @Test
    public void subCategoryEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSubCategory_ShowsSubCategory() {
        if (mItem.getSubCategory() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSubCategory())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("SubCategory text not displayed.");
            }
        }
    }

    @Test
    public void subTypeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSubType_ShowsSubType() {
        if (mItem.getSubtype() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSubtype())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("SubType text not displayed.");
            }
        }
    }

    @Test
    public void subType2EditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSubType2_ShowsSubType2() {
        if (mItem.getSubtype2() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSubtype2())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("SubType2 text not displayed.");
            }
        }
    }

    @Test
    public void subType3EditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSubType3_ShowsSubType() {
        if (mItem.getSubtype3() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSubtype3())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("SubType3 text not displayed.");
            }
        }
    }

    @Test
    public void primaryColourShadeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_primary_colour_shade)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemHasPrimaryColourShade_ShowsPrimaryColourShade() {
        if (mItem.getPrimaryColourShade() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getPrimaryColourShade())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("PrimaryColourShade text not displayed.");
            }
        }
    }

    @Test
    public void secondaryColourEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_secondary_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSecondaryColour_ShowsSecondaryColour() {
        if (mItem.getSecondaryColour() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSecondaryColour())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("SecondaryColour text not displayed.");
            }
        }
    }

    @Test
    public void sizeEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasSize_ShowsSize() {
        if (mItem.getSize() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getSize())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Size text not displayed.");
            }
        }
    }

    @Test
    public void brandEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasBrand_ShowsBrand() {
        if (mItem.getBrand() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getBrand())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Brand text not displayed.");
            }
        }
    }

    @Test
    public void shopEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasShop_ShowsShop() {
        if (mItem.getShop() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getShop())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Shop text not displayed.");
            }
        }
    }

    @Test
    public void descriptionEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasDescription_ShowsDescription() {
        if (mItem.getDescription() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getDescription())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Description text not displayed.");
            }
        }
    }

    @Test
    public void noteEditTextVisible() {
        closeSoftKeyboard();
        onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void itemHasNote_ShowsNote() {
        if (mItem.getNote() != null) {
            closeSoftKeyboard();
            try {
                onView(withText(mItem.getNote())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            } catch (PerformException e) {
                fail("Note text not displayed.");
            }
        }
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
            } catch (PerformException ignore) {
            }
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

    @Test
    public void takePicture_ShowsPicture() {
        // Stub a result from taking a picture with the camera
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Make sure to close the keyboard otherwise scrolling won't work
        closeSoftKeyboard();

        // Take picture with camera
        onView(withId(R.id.action_take_photo)).perform(click());

        // Check that the image is displayed in the UI
        onView(withId(R.id.edit_item_image)).perform(scrollTo())
                .check(matches(allOf(
                        hasDrawable(),
                        isDisplayed())));
    }

    @Test
    public void selectImage_ShowsImage() {
        stubResultFromSelectingImagePicker();

        // Click to select an image
        onView(withId(R.id.action_select_image)).perform(click());

        // Check that the image is displayed in the UI
        onView(withId(R.id.edit_item_image)).perform(scrollTo())
                .check(matches(allOf(
                        hasDrawable(),
                        isDisplayed())));
    }

    @Test
    public void clickRemoveImage_RemovesImage() {
        stubResultFromSelectingImagePicker();

        // Click to select an image
        onView(withId(R.id.action_select_image)).perform(click());

        // Click remove image
        onView(withId(R.id.edit_item_remove_image_button)).perform(scrollTo()).perform(click());

        // Check the image is not displayed
        try {
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            not(isDisplayed()))));
        } catch (PerformException ignore) {
        }

        // Check the remove image button is not displayed
        try {
            onView(withId(R.id.edit_item_remove_image_button)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            not(isDisplayed()))));
        } catch (PerformException ignore) {
        }
    }

    private void stubResultFromSelectingImagePicker() {
        Intent resultData = new Intent();
        FakeImageFileImpl fakeImageFile = new FakeImageFileImpl();
        fakeImageFile.create(mActivityRule.getActivity(), "fake_image", ".jpg");
        Uri selectedImageUri = fakeImageFile.getUri();
        resultData.setData(selectedImageUri);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result);

        // Make sure to close the keyboard otherwise scrolling won't work
        closeSoftKeyboard();
    }
}
