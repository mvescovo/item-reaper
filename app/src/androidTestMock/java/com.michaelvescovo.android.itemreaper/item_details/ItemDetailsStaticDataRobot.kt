package com.michaelvescovo.android.itemreaper.item_details

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.michaelvescovo.android.itemreaper.R
import org.hamcrest.Matchers.allOf

/**
 * Created by Michael Vescovo.
 */

fun staticData(func: ItemDetailsStaticDataRobot.() -> Unit): ItemDetailsStaticDataRobot {
    return ItemDetailsStaticDataRobot().apply { func() }
}

class ItemDetailsStaticDataRobot {

    fun upNavigation() {
        onView(withContentDescription("Navigate up")).check(matches(isDisplayed()))
    }

    fun title(title: Int) {
        onView(withId(R.id.appbar_title)).check(matches(allOf(isDisplayed(), withText(title))))
    }

    fun expireItemMenuOption() {
        onView(withId(R.id.action_expire_item)).check(matches(isDisplayed()))
    }

    fun editItemMenuOption() {
        onView(withId(R.id.action_edit_item)).check(matches(isDisplayed()))
    }

    fun adMob() {
        onView(withId(R.id.ad_view_container)).check(matches(isDisplayed()))
    }

    fun purchaseDetailsTitle(purchaseDetailsTitle: Int) {
        onView(withId(R.id.purchase_details_title)).perform(ViewActions.scrollTo())
                .check(matches(allOf(isDisplayed(), withText(purchaseDetailsTitle))))
    }

    fun itemDetailsTitle(itemDetailsTitle: Int) {
        onView(withId(R.id.item_details_title)).perform(ViewActions.scrollTo())
                .check(matches(allOf(isDisplayed(), withText(itemDetailsTitle))))
    }
}