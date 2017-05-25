package com.michaelvescovo.android.itemreaper.edit_item;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoHelperMethods;
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.matcher.AdapterHasDataMatcher.withAdaptedData;
import static com.michaelvescovo.android.itemreaper.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditItemScreenTest {

    @Rule
    public IntentsTestRule<EditItemActivity> mActivityRule =
            new IntentsTestRule<>(EditItemActivity.class);

    private EspressoHelperMethods mEspressoHelperMethods;

    @Before
    public void setup() {
        mEspressoHelperMethods = new EspressoHelperMethods(
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
        onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDetailsTitleVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_details_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_details_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateTitleVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_purchase_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void purchaseDateSpinnerVisible() {
        dateSpinnerVisible(R.id.purchase_date_spinner);
    }

    @Test
    public void expiryDateSpinnerVisible() {
        dateSpinnerVisible(R.id.expiry_date_spinner);
    }

    private void dateSpinnerVisible(int spinnerId) {
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickPurchaseDateSpinner_ShowsCorrectOptions() {
        clickDateSpinner_ShowsCorrectOptions(R.id.purchase_date_spinner);
    }

    @Test
    public void clickExpiryDateSpinner_ShowsCorrectOptions() {
        clickDateSpinner_ShowsCorrectOptions(R.id.expiry_date_spinner);
    }

    private void clickDateSpinner_ShowsCorrectOptions(int spinnerId) {
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_today))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_yesterday))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_custom))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_unknown))))
                .check(matches(isDisplayed()));

        Espresso.pressBack();
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo()).perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_today))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_yesterday))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_custom))))
                .check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_unknown))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void selectPurchaseDateToday_TodaySelected() {
        selectDateToday_TodaySelected(R.id.purchase_date_spinner);
    }

    @Test
    public void selectExpiryDateToday_TodaySelected() {
        selectDateToday_TodaySelected(R.id.expiry_date_spinner);
    }

    private void selectDateToday_TodaySelected(int spinnerId) {
        selectDateToday(spinnerId);
        confirmDateTodaySelected(spinnerId);
        mEspressoHelperMethods.rotateScreen();
        confirmDateTodaySelected(spinnerId);
    }

    private void selectDateToday(int spinnerId) {
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_today))))
                .perform(click());
    }

    private void confirmDateTodaySelected(int spinnerId) {
        onView(withId(spinnerId))
                .check(matches(withSpinnerText(containsString(
                        mEspressoHelperMethods.getResourceString(R.string.edit_date_today)))));
    }

    @Test
    public void selectPurchaseDateYesterday_YesterdaySelected() {
        selectDateYesterday(R.id.purchase_date_spinner);
        confirmDateYesterdaySelected(R.id.purchase_date_spinner);
        mEspressoHelperMethods.rotateScreen();
        confirmDateYesterdaySelected(R.id.purchase_date_spinner);
    }

    @Test
    public void selectExpiryDateYesterday_YesterdaySelected() {
        selectDateYesterday(R.id.expiry_date_spinner);
        confirmDateYesterdaySelected(R.id.expiry_date_spinner);
        mEspressoHelperMethods.rotateScreen();
        confirmDateYesterdaySelected(R.id.expiry_date_spinner);
    }

    private void selectDateYesterday(int spinnerId) {
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_yesterday))))
                .perform(click());
    }

    private void confirmDateYesterdaySelected(int spinnerId) {
        onView(withId(spinnerId))
                .check(matches(withSpinnerText(containsString(
                        mEspressoHelperMethods.getResourceString(
                                R.string.edit_date_yesterday)))));
    }

    @Test
    public void selectPurchaseDateCustom_CustomDateSelected() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(ITEM_1.getPurchaseDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        mEspressoHelperMethods.rotateScreen();
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
    }

    @Test
    public void selectExpiryDateCustom_CustomDateSelected() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(ITEM_1.getExpiry());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        mEspressoHelperMethods.rotateScreen();
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
    }

    private void selectDateCustom(int spinnerId, @Nullable Calendar customDate, boolean ok) {
        Espresso.closeSoftKeyboard();

        onView(withId(spinnerId)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_custom))))
                .perform(click());

        if (customDate != null) {
            onView(isAssignableFrom(DatePicker.class)).perform(setDate(
                    customDate.get(Calendar.YEAR),
                    customDate.get(Calendar.MONTH) + 1, // Months start at 0.
                    customDate.get(Calendar.DAY_OF_MONTH)));
        }

        if (ok) {
            onView(withId(android.R.id.button1)).perform(click());
        } else {
            onView(withId(android.R.id.button2)).perform(click());
        }
    }

    @Test
    public void selectPurchaseDateCustom_ClickCancel_PreviousSelectionDisplayed() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(ITEM_1.getPurchaseDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        selectDateToday(R.id.purchase_date_spinner);
        confirmDateTodaySelected(R.id.purchase_date_spinner);
        selectDateCustom(R.id.purchase_date_spinner, null, false); // Press cancel on custom
        confirmDateTodaySelected(R.id.purchase_date_spinner);

        selectDateYesterday(R.id.purchase_date_spinner);
        confirmDateYesterdaySelected(R.id.purchase_date_spinner);
        selectDateCustom(R.id.purchase_date_spinner, null, false); // Press cancel on custom
        confirmDateYesterdaySelected(R.id.purchase_date_spinner);

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        selectDateCustom(R.id.purchase_date_spinner, null, false); // Press cancel on custom
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);

        selectDateUnknown(R.id.purchase_date_spinner);
        confirmDateUnknownSelected(R.id.purchase_date_spinner);
        selectDateCustom(R.id.purchase_date_spinner, null, false); // Press cancel on custom
        confirmDateUnknownSelected(R.id.purchase_date_spinner);
    }

    @Test
    public void selectExpiryDateCustom_ClickCancel_PreviousSelectionDisplayed() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(ITEM_1.getExpiry());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        selectDateToday(R.id.expiry_date_spinner);
        confirmDateTodaySelected(R.id.expiry_date_spinner);
        selectDateCustom(R.id.expiry_date_spinner, null, false); // Press cancel on custom
        confirmDateTodaySelected(R.id.expiry_date_spinner);

        selectDateYesterday(R.id.expiry_date_spinner);
        confirmDateYesterdaySelected(R.id.expiry_date_spinner);
        selectDateCustom(R.id.expiry_date_spinner, null, false); // Press cancel on custom
        confirmDateYesterdaySelected(R.id.expiry_date_spinner);

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        selectDateCustom(R.id.expiry_date_spinner, null, false); // Press cancel on custom
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);

        selectDateUnknown(R.id.expiry_date_spinner);
        confirmDateUnknownSelected(R.id.expiry_date_spinner);
        selectDateCustom(R.id.expiry_date_spinner, null, false); // Press cancel on custom
        confirmDateUnknownSelected(R.id.expiry_date_spinner);
    }

    @Test
    public void selectPurchaseDateCustom_SelectDifferentOption_CustomDateRemovedFromSpinner() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(1452776400000L); // 15/1/2016
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        Calendar customDate2 = Calendar.getInstance();
        customDate2.setTimeInMillis(1496325600000L); // 2/6/2017

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        selectDateToday(R.id.purchase_date_spinner);
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, customDateString);

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        selectDateYesterday(R.id.purchase_date_spinner);
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, customDateString);

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        selectDateCustom(R.id.purchase_date_spinner, customDate2, true);
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, customDateString);

        selectDateCustom(R.id.purchase_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.purchase_date_spinner, customDateString);
        selectDateUnknown(R.id.purchase_date_spinner);
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, customDateString);
    }

    @Test
    public void selectExpiryCustomDate_SelectDifferentOption_CustomDateRemovedFromSpinner() {
        Calendar customDate = Calendar.getInstance();
        customDate.setTimeInMillis(1452776400000L); // 15/1/2016
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MMM/YY", Locale.ENGLISH);
        String customDateString = simpleDateFormat.format(customDate.getTime());

        Calendar customDate2 = Calendar.getInstance();
        customDate2.setTimeInMillis(1496325600000L); // 2/6/2017

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        selectDateToday(R.id.expiry_date_spinner);
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, customDateString);

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        selectDateYesterday(R.id.expiry_date_spinner);
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, customDateString);

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        selectDateCustom(R.id.expiry_date_spinner, customDate2, true);
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, customDateString);

        selectDateCustom(R.id.expiry_date_spinner, customDate, true);
        confirmDateCustomSelected(R.id.expiry_date_spinner, customDateString);
        selectDateUnknown(R.id.expiry_date_spinner);
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, customDateString);
    }

    private void confirmCustomDateNotInSpinner(int spinnerId, String customDateString) {
        onView(withId(spinnerId))
                .check(matches(not(withAdaptedData(allOf(is(instanceOf(String.class)),
                        is(customDateString))))));
    }

    @Test
    public void selectPurchaseDateUnknown_UnknownSelected() {
        selectDateUnknown(R.id.purchase_date_spinner);
        confirmDateUnknownSelected(R.id.purchase_date_spinner);
        mEspressoHelperMethods.rotateScreen();
        confirmDateUnknownSelected(R.id.purchase_date_spinner);
    }

    @Test
    public void selectExpiryDateUnknown_UnknownSelected() {
        selectDateUnknown(R.id.expiry_date_spinner);
        confirmDateUnknownSelected(R.id.expiry_date_spinner);
        mEspressoHelperMethods.rotateScreen();
        confirmDateUnknownSelected(R.id.expiry_date_spinner);
    }

    private void selectDateUnknown(int spinnerId) {
        Espresso.closeSoftKeyboard();
        onView(withId(spinnerId)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(mEspressoHelperMethods.getResourceString(R.string.edit_date_unknown))))
                .perform(click());
    }

    private void confirmDateUnknownSelected(int spinnerId) {
        onView(withId(spinnerId))
                .check(matches(withSpinnerText(containsString(
                        mEspressoHelperMethods.getResourceString(
                                R.string.edit_date_unknown)))));
    }

    @Test
    public void shopEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_shop)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void pricePaidEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_price_paid)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void discountEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_discount)).perform(scrollTo()).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemDetailsTitleVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_item_details_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_item_details_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void expiryTitleTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_expiry_date_title)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void categoryEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_category)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subCategoryEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_category)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_category)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void typeEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subTypeEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType2EditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type2)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void subType3EditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_sub_type3)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void mainColourEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_main_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_main_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void mainColourShadeEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_main_colour_shade)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_main_colour_shade)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void accentColourEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_accent_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_accent_colour)).perform(scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void sizeEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_size)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void brandEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_brand)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void descriptionEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_description)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void noteEditTextVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.edit_note)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void takePhotoMenuOptionVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()));
    }

    @Test
    public void selectImageMenuOptionVisible() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteItemMenuOptionVisible() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()));
        mEspressoHelperMethods.rotateScreen();
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

        mEspressoHelperMethods.rotateScreen();
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

        mEspressoHelperMethods.rotateScreen();
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

        mEspressoHelperMethods.rotateScreen();
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
        Uri selectedImageUri = fakeImageFile.getUri(mActivityRule.getActivity());
        resultData.setData(selectedImageUri);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result);
    }

    private static void confirmDateCustomSelected(int spinnerId, String customDateString) {
        onView(withId(spinnerId))
                .check(matches(withSpinnerText(containsString(customDateString))));
    }
}
