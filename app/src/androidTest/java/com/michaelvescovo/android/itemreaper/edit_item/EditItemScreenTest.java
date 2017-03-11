package com.michaelvescovo.android.itemreaper.edit_item;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl;
import com.michaelvescovo.android.itemreaper.util.RotationHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_ID_1;
import static com.michaelvescovo.android.itemreaper.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(Enclosed.class)
@LargeTest
public class EditItemScreenTest {

    /*
    * Only run these tests once.
    * */
    @RunWith(JUnit4.class)
    public static class StandardEditItemScreenTest {

        @Rule
        public IntentsTestRule<EditItemActivity> mActivityRule =
                new IntentsTestRule<>(EditItemActivity.class, true, false);

        private RotationHelper mRotationHelper;

        @Before
        public void setup() {
            Intent intent = new Intent();
            intent.putExtra(EditItemActivity.EXTRA_ITEM_ID, ITEM_ID_1);
            mActivityRule.launchActivity(intent);
            mRotationHelper = new RotationHelper(InstrumentationRegistry.getTargetContext(),
                    mActivityRule.getActivity());
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
            onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
        }

        @Test
        public void purchaseDetailsTitleVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_purchase_details_title)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_purchase_details_title)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void purchaseDateTitleVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void purchaseDateSpinnerVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.purchase_date_spinner)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.purchase_date_spinner)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void shopEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void pricePaidEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void discountEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void itemDetailsTitleVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_item_details_title)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_item_details_title)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void expiryTitleTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void expiryDateSpinnerVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.expiry_date_spinner)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withId(R.id.expiry_date_spinner)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void categoryEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void subCategoryEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_category)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_category)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void typeEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void subTypeEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void subType2EditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void subType3EditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void primaryColourEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_primary_colour)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_primary_colour)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void primaryColourShadeEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_primary_colour_shade)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_primary_colour_shade)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void secondaryColourEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_secondary_colour)).perform(scrollTo())
                    .check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_secondary_colour)).perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

        @Test
        public void sizeEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void brandEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void descriptionEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void noteEditTextVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
        }

        @Test
        public void takePhotoMenuOptionVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()));
        }

        @Test
        public void selectImageMenuOptionVisible() {
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.action_select_image)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.action_select_image)).check(matches(isDisplayed()));
        }

        @Test
        public void deleteItemMenuOptionVisible() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()));
        }

        @Test
        public void takePicture_ShowsPicture() {
            // Stub a result from taking a picture with the camera
            Instrumentation.ActivityResult result =
                    new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
            intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

            // Make sure to close the keyboard otherwise scrolling won't work
            Espresso.closeSoftKeyboard();

            // Take picture with camera
            onView(withId(R.id.action_take_photo)).perform(click());

            // Check that the image is displayed in the UI
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));

            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();

            // Check that the image is displayed in the UI
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));

            // Click remove image
            onView(withId(R.id.edit_item_remove_image_button)).perform(scrollTo()).perform(click());
        }

        @Test
        public void selectImage_ShowsImage() {
            stubResultFromSelectingImagePicker();
            Espresso.closeSoftKeyboard();

            // Click to select an image
            onView(withId(R.id.action_select_image)).perform(click());

            // Check that the image is displayed in the UI
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));

            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();

            // Check that the image is displayed in the UI
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));

            // Click remove image
            onView(withId(R.id.edit_item_remove_image_button)).perform(scrollTo()).perform(click());
        }

        @Test
        public void clickRemoveImage_RemovesImage() {
            stubResultFromSelectingImagePicker();
            Espresso.closeSoftKeyboard();

            // Click to select an image
            onView(withId(R.id.action_select_image)).perform(click());

            // Check that the image is displayed in the UI
            onView(withId(R.id.edit_item_image)).perform(scrollTo())
                    .check(matches(allOf(
                            hasDrawable(),
                            isDisplayed())));

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

            mRotationHelper.rotateScreen();
            Espresso.closeSoftKeyboard();

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
        }
    }

    /*
    * Run these tests for each parameter.
    * */
    @RunWith(Parameterized.class)
    public static class ParameterisedEditItemScreenTest {

        @Rule
        public IntentsTestRule<EditItemActivity> mActivityRule =
                new IntentsTestRule<>(EditItemActivity.class, true, false);

        private RotationHelper mRotationHelper;
        private Item mItem;

        public ParameterisedEditItemScreenTest(Item item) {
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
            mRotationHelper = new RotationHelper(InstrumentationRegistry.getTargetContext(),
                    mActivityRule.getActivity());
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

//        @Test
//        public void itemHasPurchaseDate_ShowsPurchaseDate() {
//            if (mItem.getPurchaseDate() != -1) {
//                Espresso.closeSoftKeyboard();
//                Calendar purchaseDate = Calendar.getInstance();
//                purchaseDate.setTimeInMillis(mItem.getPurchaseDate());
//                int day = purchaseDate.get(Calendar.DAY_OF_MONTH);
//                int month = purchaseDate.get(Calendar.MONTH);
//                month++; // Months start at 0.
//                int year = purchaseDate.get(Calendar.YEAR);
//
//                onView(withText(String.valueOf(day))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(month))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(year))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//
//                mRotationHelper.rotateScreen();
//                Espresso.closeSoftKeyboard();
//
//                onView(withText(String.valueOf(day))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(month))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(year))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//            }
//        }

        @Test
        public void itemHasShop_ShowsShop() {
            if (mItem.getShop() != null) {
                Espresso.closeSoftKeyboard();
                onView(withText(mItem.getShop())).perform(scrollTo())
                        .check(matches(isDisplayed()));
                mRotationHelper.rotateScreen();
                Espresso.closeSoftKeyboard();
                onView(withText(mItem.getShop())).perform(scrollTo())
                        .check(matches(isDisplayed()));
            }
        }

        @Test
        public void itemHasPrice_ShowsPrice() {
            if (mItem.getPricePaid() != -1) {
                Espresso.closeSoftKeyboard();
                onView(withText(String.valueOf(mItem.getPricePaid()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                mRotationHelper.rotateScreen();
                Espresso.closeSoftKeyboard();
                onView(withText(String.valueOf(mItem.getPricePaid()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            }
        }

        @Test
        public void itemHasDiscount_ShowsDiscount() {
            if (mItem.getDiscount() != -1) {
                Espresso.closeSoftKeyboard();
                onView(withText(String.valueOf(mItem.getDiscount()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
                mRotationHelper.rotateScreen();
                Espresso.closeSoftKeyboard();
                onView(withText(String.valueOf(mItem.getDiscount()))).perform(scrollTo())
                        .check(matches(isDisplayed()));
            }
        }

//        @Test
//        public void itemHasExpiry_ShowsExpiry() {
//            if (mItem.getExpiry() != -1) {
//                Espresso.closeSoftKeyboard();
//                Calendar expiry = Calendar.getInstance();
//                expiry.setTimeInMillis(mItem.getExpiry());
//                int day = expiry.get(Calendar.DAY_OF_MONTH);
//                int month = expiry.get(Calendar.MONTH);
//                month++; // Months start at 0.
//                int year = expiry.get(Calendar.YEAR);
//
//                onView(withText(String.valueOf(day))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(month))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(year))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//
//                mRotationHelper.rotateScreen();
//                Espresso.closeSoftKeyboard();
//
//                onView(withText(String.valueOf(day))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(month))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//                onView(withText(String.valueOf(year))).perform(scrollTo())
//                        .check(matches(isDisplayed()));
//            }
//        }

        @Test
        public void itemHasCategory_ShowsCategory() {
            if (mItem.getCategory() != null) {
                Espresso.closeSoftKeyboard();
                onView(withText(mItem.getCategory())).perform(scrollTo())
                        .check(matches(isDisplayed()));
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
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
                mRotationHelper.rotateScreen();
                Espresso.closeSoftKeyboard();
                onView(withId(R.id.edit_item_image)).perform(scrollTo())
                        .check(matches(allOf(
                                hasDrawable(),
                                isDisplayed())));
            }
        }
    }
}
