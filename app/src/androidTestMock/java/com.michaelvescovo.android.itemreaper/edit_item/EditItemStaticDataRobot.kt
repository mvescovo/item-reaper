package com.michaelvescovo.android.itemreaper.edit_item

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers.withTextInputLayoutHint
import org.hamcrest.Matchers.*



/**
 * Created by Michael Vescovo.
 */

fun staticData(func: EditItemStaticDataRobot.() -> Unit): EditItemStaticDataRobot {
    return EditItemStaticDataRobot().apply { func() }
}

class EditItemStaticDataRobot {

    private val mContext: Context = InstrumentationRegistry.getTargetContext()

    fun title(title: Int) {
        onView(withText(title)).check(matches(isDisplayed()))
    }

    fun takePhotoMenuOption() {
        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()))
    }

    fun selectImageMenuOption() {
        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()))
    }

    fun deleteItemMenuOption() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()))
        pressBack()
    }

    fun purchaseDetailsTitle(purchaseDetailsTitle: Int) {
        onView(withText(purchaseDetailsTitle)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun purchaseDateTitle(purchaseDateTitle: Int) {
        onView(withText(purchaseDateTitle)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun purchaseDateSpinner(today: Int, yesterday: Int, custom: Int, unknown: Int) {
        clickDateSpinner_ShowsDefaultOptions(R.id.purchase_date_spinner, listOf(
                today, yesterday, custom, unknown))
    }

    fun shopHint(shopHint: Int) {
        onView(withTextInputLayoutHint(shopHint)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun pricePaidHint(pricePaidHint: Int) {
        onView(withTextInputLayoutHint(pricePaidHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun discountHint(discountHint: Int) {
        onView(withTextInputLayoutHint(discountHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun itemDetailsTitle(itemDetailsTitle: Int) {
        onView(withText(itemDetailsTitle)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun expiryDateTitle(expiryDateTitle: Int) {
        onView(withText(expiryDateTitle)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun expiryDateSpinner(today: Int, yesterday: Int, custom: Int, unknown: Int) {
        clickDateSpinner_ShowsDefaultOptions(R.id.expiry_date_spinner, listOf(
                today, yesterday, custom, unknown))
    }

    fun categoryHint(categoryHint: Int) {
        onView(withTextInputLayoutHint(categoryHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun subCategoryHint(subCategoryHint: Int) {
        onView(withTextInputLayoutHint(subCategoryHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun typeHint(typeHint: Int) {
        onView(withTextInputLayoutHint(typeHint)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    fun subTypeHint(subTypeHint: Int) {
        onView(withTextInputLayoutHint(subTypeHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun subType2Hint(subType2Hint: Int) {
        onView(withTextInputLayoutHint(subType2Hint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun subType3Hint(subType3Hint: Int) {
        onView(withTextInputLayoutHint(subType3Hint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun mainColourHint(mainColourHint: Int) {
        onView(withTextInputLayoutHint(mainColourHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun mainColourShadeHint(mainColourShadeHint: Int) {
        onView(withTextInputLayoutHint(mainColourShadeHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun accentColourHint(accentColourHint: Int) {
        onView(withTextInputLayoutHint(accentColourHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun sizeHint(sizeHint: Int) {
        onView(withTextInputLayoutHint(sizeHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun brandHint(brandHint: Int) {
        onView(withTextInputLayoutHint(brandHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun descriptionHint(descriptionHint: Int) {
        onView(withTextInputLayoutHint(descriptionHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    fun noteHint(noteHint: Int) {
        onView(withTextInputLayoutHint(noteHint)).perform(scrollTo())
                .check(matches(isDisplayed()))
    }

    private fun clickDateSpinner_ShowsDefaultOptions(spinnerId: Int, options: List<Int>) {
        Espresso.closeSoftKeyboard()
        onView(withId(spinnerId)).perform(scrollTo()).perform(click())
        options.forEach {
            onData(allOf(
                    `is`(instanceOf<Any>(String::class.java)), `is`(mContext.getString(it))))
                    .check(matches(isDisplayed()))
        }
        pressBack();
    }

    //
//
//    @Test
//    fun takePhotoMenuOptionVisible() {
//        Espresso.closeSoftKeyboard()
//        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()))
//        mEspressoHelper!!.rotateScreen()
//        Espresso.closeSoftKeyboard()
//        onView(withId(R.id.action_take_photo)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun selectImageMenuOptionVisible() {
//        Espresso.closeSoftKeyboard()
//        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()))
//        mEspressoHelper!!.rotateScreen()
//        Espresso.closeSoftKeyboard()
//        onView(withId(R.id.action_select_image)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun deleteItemMenuOptionVisible() {
//        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
//        onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()))
//        mEspressoHelper!!.rotateScreen()
//        onView(withText(R.string.menu_delete_item)).check(matches(isDisplayed()))
//    }
}