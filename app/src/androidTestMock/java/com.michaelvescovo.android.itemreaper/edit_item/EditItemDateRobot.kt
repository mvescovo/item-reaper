package com.michaelvescovo.android.itemreaper.edit_item

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
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
    private val PURCHASE_DATE: String = "purchaseDate"
    private val EXPIRY_DATE: String = "expiryDate"

    fun selectPurchaseDateToday() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_today), null,
                null)
    }

    fun purchaseDateTodaySelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_today))
    }

    fun selectPurchaseDateYesterday() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_yesterday),
                null, null)
    }

    fun purchaseDateYesterdaySelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_yesterday))
    }

    fun enterCustomPurchaseDate(date: String, selectCustomDate: Boolean?) {
        Espresso.closeSoftKeyboard()
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        val customDate = Calendar.getInstance()
        customDate.time = simpleDateFormat.parse(date)
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_custom),
                customDate, selectCustomDate)
    }

    fun purchaseDateCustomSelected(date: String) {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.purchase_date_spinner, date)
    }

    fun customPurchaseDateNotInSpinner(date: String) {
        Espresso.closeSoftKeyboard()
        confirmCustomDateNotInSpinner(R.id.purchase_date_spinner, date)
    }

    fun selectPurchaseDateUnknown() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.purchase_date_spinner, mContext.getString(R.string.edit_date_unknown), null,
                null)
    }

    fun purchaseDateUnknownSelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.purchase_date_spinner,
                mContext.getString(R.string.edit_date_unknown))
    }

    fun correctPurchaseDateShown(date: Long) {
        Espresso.closeSoftKeyboard()
        correctDateShown(date, PURCHASE_DATE)
    }

    fun selectExpiryDateToday() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_today), null,
                null)
    }

    fun expiryDateTodaySelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_today))
    }

    fun selectExpiryDateYesterday() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_yesterday), null,
                null)
    }

    fun expiryDateYesterdaySelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.expiry_date_spinner,
                mContext.getString(R.string.edit_date_yesterday))
    }

    fun enterCustomExpiryDate(date: String, selectCustomDate: Boolean?) {
        Espresso.closeSoftKeyboard()
        val customDate = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        customDate.time = simpleDateFormat.parse(date)
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_custom),
                customDate, selectCustomDate)
    }

    fun expiryDateCustomSelected(date: String) {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.expiry_date_spinner, date)

    }

    fun customExpiryDateNotInSpinner(date: String) {
        Espresso.closeSoftKeyboard()
        confirmCustomDateNotInSpinner(R.id.expiry_date_spinner, date)
    }

    fun selectExpiryDateUnknown() {
        Espresso.closeSoftKeyboard()
        selectDate(R.id.expiry_date_spinner, mContext.getString(R.string.edit_date_unknown), null,
                null)
    }

    fun expiryDateUnknownSelected() {
        Espresso.closeSoftKeyboard()
        confirmDateSelected(R.id.expiry_date_spinner,
                mContext.getString(R.string.edit_date_unknown))
    }

    fun correctExpiryDateShown(date: Long) {
        Espresso.closeSoftKeyboard()
        correctDateShown(date, EXPIRY_DATE)
    }

    private fun selectDate(spinnerId: Int, spinnerOption: String, customDate: Calendar?,
                           ok: Boolean?) {
        Espresso.closeSoftKeyboard()
        onView(withId(spinnerId)).perform(scrollTo()).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)),
                `is`(spinnerOption)))
                .perform(ViewActions.click())

        if (customDate != null) {
            Espresso.closeSoftKeyboard()
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
        Espresso.closeSoftKeyboard()
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(withSpinnerText(containsString(spinnerOption))))
    }

    private fun confirmCustomDateNotInSpinner(spinnerId: Int, customDateString: String) {
        Espresso.closeSoftKeyboard()
        onView(withId(spinnerId)).perform(scrollTo())
                .check(matches(not<View>(withAdaptedData(allOf(
                        `is`(instanceOf<Any>(String::class.java)), `is`(customDateString))))))
    }

    fun correctDateShown(date: Long, dateType: String) {
        Espresso.closeSoftKeyboard()
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        val currentDate = Calendar.getInstance()
        val todayDateString = simpleDateFormat.format(currentDate.time)
        currentDate.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayDateString = simpleDateFormat.format(currentDate.time)

        if (date != -1L) {
            val purchaseDate = Calendar.getInstance()
            purchaseDate.timeInMillis = date
            val purchaseDateString = simpleDateFormat.format(purchaseDate.time)

            if (purchaseDateString == todayDateString) {
                when (dateType) {
                    PURCHASE_DATE -> purchaseDateTodaySelected()
                    EXPIRY_DATE -> expiryDateTodaySelected()
                }
            } else if (purchaseDateString == yesterdayDateString) {
                when (dateType) {
                    PURCHASE_DATE -> purchaseDateYesterdaySelected()
                    EXPIRY_DATE -> expiryDateYesterdaySelected()
                }
            } else {
                when (dateType) {
                    PURCHASE_DATE -> purchaseDateCustomSelected(purchaseDateString)
                    EXPIRY_DATE -> expiryDateCustomSelected(purchaseDateString)
                }
            }
        } else {
            when (dateType) {
                PURCHASE_DATE -> purchaseDateUnknownSelected()
                EXPIRY_DATE -> expiryDateUnknownSelected()
            }
        }
    }
}
