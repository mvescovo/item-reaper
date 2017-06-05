package com.michaelvescovo.android.itemreaper.edit_item

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.PickerActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import android.widget.DatePicker
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers.withAdaptedData
import org.hamcrest.Matchers.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Michael Vescovo.
 */

fun date(func: EditItemDateRobot.() -> Unit): EditItemDateRobot {
    return EditItemDateRobot().apply { func() }
}

class EditItemDateRobot {

    private val mContext: Context = InstrumentationRegistry.getTargetContext()

    fun selectPurchaseDateToday() {
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_today), null,
                null)
    }

    fun purchaseDateTodaySelected() {
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_today))
    }

    fun selectPurchaseDateYesterday() {
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_yesterday),
                null, null)
    }

    fun purchaseDateYesterdaySelected() {
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_yesterday))
    }

    fun enterCustomPurchaseDate(date: String, selectCustomDate: Boolean?) {
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        val customDate = Calendar.getInstance()
        customDate.time = simpleDateFormat.parse(date)
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_custom),
                customDate, selectCustomDate)
    }

    fun purchaseDateCustomSelected(date: String) {
        confirmDateSelected(R.id.purchase_date_spinner, date)
    }

    fun customPurchaseDateNotInSpinner(date: String) {
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, date)
    }

    fun selectPurchaseDateUnknown() {
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_unknown), null,
                null)
    }

    fun purchaseDateUnknownSelected() {
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_unknown))
    }

    fun selectExpiryDateToday() {
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_today), null,
                null)
    }

    fun expiryDateTodaySelected() {
        confirmDateSelected(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_today))
    }

    fun selectExpiryDateYesterday() {
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_yesterday), null,
                null)
    }

    fun expiryDateYesterdaySelected() {
        confirmDateSelected(R.id.expiry_date_spinner,
                mContext.getString(R.string.edit_date_yesterday))
    }

    fun enterCustomExpiryDate(date: String, selectCustomDate: Boolean?) {
        val customDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        customDate.time = simpleDateFormat.parse(date)
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_custom),
                customDate, selectCustomDate)
    }

    fun expiryDateCustomSelected(date: String) {
        confirmDateSelected(R.id.expiry_date_spinner, date)

    }

    fun customExpiryDateNotInSpinner(date: String) {
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, date)
    }

    fun selectExpiryDateUnknown() {
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_unknown), null,
                null)
    }

    fun expiryDateUnknownSelected() {
        confirmDateSelected(R.id.expiry_date_spinner,
                mContext.getString(R.string.edit_date_unknown))
    }

    private fun selectDate(spinnerId: Int, spinnerOption: String, customDate: Calendar?,
                           ok: Boolean?) {
        onView(withId(spinnerId)).perform(scrollTo()).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)),
                `is`(spinnerOption)))
                .perform(ViewActions.click())

        if (customDate != null) {
            onView(isAssignableFrom(DatePicker::class.java)).perform(PickerActions.setDate(
                    customDate.get(Calendar.YEAR),
                    customDate.get(Calendar.MONTH) + 1, // Months start at 0.
                    customDate.get(Calendar.DAY_OF_MONTH)))
            if (ok != null && ok) {
                onView(withId(android.R.id.button1)).perform(ViewActions.click())
            } else {
                onView(withId(android.R.id.button2)).perform(ViewActions.click())
            }
        }
    }

    private fun confirmDateSelected(spinnerId: Int, spinnerOption: String) {
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(withSpinnerText(containsString(spinnerOption))))
    }

    private fun confirmCustomDateNotInSpinner(spinnerId: Int, customDateString: String) {
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(not<View>(withAdaptedData(allOf(
                        `is`(instanceOf<Any>(String::class.java)), `is`(customDateString))))))
    }
}
