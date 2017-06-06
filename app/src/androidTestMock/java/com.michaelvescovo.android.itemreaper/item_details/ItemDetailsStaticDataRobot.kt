package com.michaelvescovo.android.itemreaper.item_details

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.allOf

/**
 * Created by Michael Vescovo.
 */

fun staticData(func: ItemDetailsStaticDataRobot.() -> Unit): ItemDetailsStaticDataRobot {
    return ItemDetailsStaticDataRobot().apply { func() }
}

class ItemDetailsStaticDataRobot {

    fun title(title: Int) {
        onView(withId(title)).check(matches(allOf(isDisplayed(), withText("Item Details"))))
    }
}