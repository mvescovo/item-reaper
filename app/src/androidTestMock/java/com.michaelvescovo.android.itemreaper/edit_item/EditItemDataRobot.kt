package com.michaelvescovo.android.itemreaper.edit_item

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents

/**
 * Created by Michael Vescovo.
 */

fun data(func: EditItemDataRobot.() -> Unit): EditItemDataRobot {
    return EditItemDataRobot().apply { func() }
}

class EditItemDataRobot {

    fun shop(shop: String?) {
        if (shop != null) {
            onView(withText(shop)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun price(price: Int) {
        if (price != -1) {
            val priceString = getPriceFromTotalCents(price)
            onView(withText(priceString)).perform(scrollTo()).check(matches(isDisplayed()))
        }
    }

    fun discount(discount: Int) {
        if (discount != -1) {
            val discountString = getPriceFromTotalCents(discount)
            onView(withText(discountString)).perform(scrollTo()).check(matches(isDisplayed()))
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