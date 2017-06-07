package com.michaelvescovo.android.itemreaper.items

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.about.AboutActivity
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity
import org.hamcrest.Matchers.allOf

/**
 * Created by Michael Vescovo.
 */

fun items(func: ItemsRobot.() -> Unit): ItemsRobot {
    return ItemsRobot().apply { func() }
}

class ItemsRobot {

    fun clickEditItemButton(isLargeScreen: Boolean) {
        onView(withId(R.id.edit_item)).perform(click())
        if (!isLargeScreen) {
            intended(hasComponent(hasClassName(EditItemActivity::class.java.name)))
        }
        Espresso.closeSoftKeyboard()
    }

    fun addItemUiLaunched() {
        onView(withId(R.id.appbar_title))
                .check(matches(allOf(withText(R.string.title_activity_edit_item), isDisplayed())))
    }

    fun deleteItem() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_delete_item)).perform(click())
    }

    fun deleteListItem(itemNumber: Int) {
        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()));
        deleteItem()
    }

    fun clickUpNavButton() {
        onView(withContentDescription("Navigate up")).perform(click())
    }

    fun showsItemsFeatureUi() {
        onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()))
    }

    fun clickAboutMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_about)).perform(click())
    }

    fun showsAboutFeatureUi(isLargeScreen: Boolean) {
        onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()))
        if (!isLargeScreen) {
            intended(hasComponent(hasClassName(AboutActivity::class.java.name)))
        }
    }

    fun clickSignOutMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.menu_sign_out)).perform(click())
    }

    fun showsAuthFeatureUi() {
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))
    }

    fun showsNoItemsText() {
        onView(withId(R.id.no_items)).check(matches(isDisplayed()))
    }
}