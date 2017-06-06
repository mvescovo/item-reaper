package com.michaelvescovo.android.itemreaper.items

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.michaelvescovo.android.itemreaper.R
import org.hamcrest.Matchers.allOf

/**
 * Created by Michael Vescovo.
 */

fun itemsStaticData(func: ItemsStaticDataRobot.() -> Unit): ItemsStaticDataRobot {
    return ItemsStaticDataRobot().apply { func() }
}

class ItemsStaticDataRobot {

    fun title(title: Int) {
        onView(withId(R.id.appbar_title)).check(matches(allOf(isDisplayed(), withText(title))))
    }

    fun sortMenuOption() {
        onView(withId(R.id.action_sort)).check(matches(isDisplayed()))
    }

    fun searchMenuOption() {
        onView(withId(R.id.action_search)).check(matches(isDisplayed()))
    }

    fun aboutMenuOption() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_about)).check(matches(isDisplayed()))
        pressBack()
    }

    fun signOutMenuOption() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_sign_out)).check(matches(isDisplayed()))
        pressBack()
    }

    fun editItemButton() {
        onView(withId(R.id.edit_item)).check(matches(isDisplayed()))
    }
}