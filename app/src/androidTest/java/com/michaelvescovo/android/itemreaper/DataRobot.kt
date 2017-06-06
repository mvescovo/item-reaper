package com.michaelvescovo.android.itemreaper

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.michaelvescovo.android.itemreaper.util.MiscHelperMethods
import org.hamcrest.Matchers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Michael Vescovo.
 */

fun data(func: DataRobot.() -> Unit): DataRobot {
    return DataRobot().apply { func() }
}

class DataRobot {

    val EDIT_MODE: String = "editMode"
    val DETAILS_MODE: String = "detailsMode"

    fun purchaseDate(purchaseDate: Long) {
        if (purchaseDate == -1L) {
            onView(ViewMatchers.withId(R.id.purchase_date_value)).perform(ViewActions.scrollTo())
                    .check(matches(Matchers.allOf(isDisplayed(), withText("-"))))
        } else {
            val simpleDateFormat = SimpleDateFormat("dd/MMMM/yy", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.timeInMillis = purchaseDate
            val dateString = simpleDateFormat.format(date.time)
            onView(ViewMatchers.withId(R.id.purchase_date_value)).perform(ViewActions.scrollTo())
                    .check(matches(Matchers.allOf(isDisplayed(), withText(dateString))))
        }
    }

    fun shop(shop: String?) {
        if (shop != null) {
            onView(withText(shop)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun price(price: Int, mode: String) {
        if (price != -1) {
            val priceString = MiscHelperMethods.getPriceFromTotalCents(price)
            when (mode) {
                EDIT_MODE -> onView(withText(priceString)).perform(scrollTo())
                        .check(matches(isDisplayed()))
                DETAILS_MODE -> onView(withText("$" + priceString)).perform(scrollTo())
                        .check(matches(isDisplayed()))
            }
        }
    }

    fun discount(discount: Int, mode: String) {
        if (discount != -1) {
            val discountString = MiscHelperMethods.getPriceFromTotalCents(discount)
            when (mode) {
                EDIT_MODE -> onView(withText(discountString)).perform(scrollTo())
                        .check(matches(isDisplayed()))
                DETAILS_MODE -> onView(withText("$" + discountString)).perform(scrollTo())
                        .check(matches(isDisplayed()))
            }

        }
    }

    fun expiryDate(expiryDate: Long) {
        if (expiryDate == -1L) {
            onView(ViewMatchers.withId(R.id.expiry_date_value)).perform(ViewActions.scrollTo())
                    .check(matches(Matchers.allOf(isDisplayed(), withText("-"))))
        } else {
            val simpleDateFormat = SimpleDateFormat("dd/MMMM/yy", Locale.ENGLISH)
            val date = Calendar.getInstance()
            date.timeInMillis = expiryDate
            val dateString = simpleDateFormat.format(date.time)
            onView(ViewMatchers.withId(R.id.expiry_date_value)).perform(ViewActions.scrollTo())
                    .check(matches(Matchers.allOf(isDisplayed(), withText(dateString))))
        }
    }

    fun category(category: String?) {
        if (category != null) {
            onView(withText(category)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun subCategory(subCategory: String?) {
        if (subCategory != null) {
            onView(withText(subCategory)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun type(type: String?) {
        if (type != null) {
            onView(withText(type)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun subType(subType: String?) {
        if (subType != null) {
            onView(withText(subType)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun subType2(subType2: String?) {
        if (subType2 != null) {
            onView(withText(subType2)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun subType3(subType3: String?) {
        if (subType3 != null) {
            onView(withText(subType3)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun mainColour(mainColour: String?) {
        if (mainColour != null) {
            onView(withText(mainColour)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun mainColourShade(mainColourShade: String?) {
        if (mainColourShade != null) {
            onView(withText(mainColourShade)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun accentColour(accentColour: String?) {
        if (accentColour != null) {
            onView(withText(accentColour)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun size(size: String?) {
        if (size != null) {
            onView(withText(size)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun brand(brand: String?) {
        if (brand != null) {
            onView(withText(brand)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun description(description: String?) {
        if (description != null) {
            onView(withText(description)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun note(note: String?) {
        if (note != null) {
            onView(withText(note)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }
}