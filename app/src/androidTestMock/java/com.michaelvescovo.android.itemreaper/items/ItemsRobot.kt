package com.michaelvescovo.android.itemreaper.items

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.PickerActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.DatePicker
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.about.AboutActivity
import com.michaelvescovo.android.itemreaper.data.Item
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl
import com.michaelvescovo.android.itemreaper.util.MiscHelperMethods
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Michael Vescovo.
 */

fun items(func: ItemsRobot.() -> Unit): ItemsRobot {
    return ItemsRobot().apply { func() }
}

class ItemsRobot {

    private val mContext = InstrumentationRegistry.getTargetContext()

    fun clickEditItemButton(isLargeScreen: Boolean) {
        Espresso.closeSoftKeyboard()
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

    fun addItem(item: Item) {
        // Click to add item
        onView(withId(R.id.edit_item)).perform(click())
        Espresso.closeSoftKeyboard()

        // Type price paid
        if (item.pricePaid != -1) {
            val priceString = MiscHelperMethods.getPriceFromTotalCents(item.pricePaid)
            onView(withId(R.id.edit_price_paid)).perform(ViewActions.scrollTo())
                    .perform(ViewActions.typeText(priceString), ViewActions.closeSoftKeyboard())
        }

        // Select expiry
        val expiry = Calendar.getInstance()
        expiry.timeInMillis = item.expiry
        onView(withId(R.id.expiry_date_spinner)).perform(ViewActions.scrollTo()).perform(click())
        Espresso.onData(AllOf.allOf(Matchers.`is`(Matchers.instanceOf<Any>(String::class.java)),
                `is`(mContext.getString(R.string.edit_date_custom))))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click())
        onView(isAssignableFrom(DatePicker::class.java)).perform(PickerActions.setDate(
                expiry.get(Calendar.YEAR),
                expiry.get(Calendar.MONTH) + 1, // Months start at 0.
                expiry.get(Calendar.DAY_OF_MONTH)))
        onView(withId(android.R.id.button1)).perform(click())

        // Type category
        onView(withId(R.id.edit_category)).perform(ViewActions.scrollTo())
                .perform(ViewActions.typeText(item.category!!), ViewActions.closeSoftKeyboard())

        // Type type
        onView(withId(R.id.edit_type)).perform(ViewActions.scrollTo())
                .perform(ViewActions.typeText(item.type!!), ViewActions.closeSoftKeyboard())

        // Type main colour
        if (item.mainColour != null) {
            onView(withId(R.id.edit_main_colour)).perform(ViewActions.scrollTo())
                    .perform(ViewActions.typeText(item.mainColour!!), ViewActions.closeSoftKeyboard())
        }

        // Add image
        if (item.imageUrl != null) {
            stubResultFromSelectingImagePicker()
            onView(withId(R.id.action_select_image)).perform(click())
        }

        // Navigate back to the list
        onView(withContentDescription("Navigate up")).perform(click())
    }

    fun showsItemInList(item: Item, isLargeScreen: Boolean) {
        // Scroll to item (category text must be unique)
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                .scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(item.category))))

        // Check editPrice paid
        if (item.pricePaid != -1) {
            val priceString = MiscHelperMethods.getPriceFromTotalCents(item.pricePaid)
            onView(withText("Paid: $$priceString")).check(matches(isDisplayed()))
        }

        // Check expiry
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        val expiry = Calendar.getInstance()
        expiry.timeInMillis = item.expiry
        onView(withText(simpleDateFormat.format(expiry.time))).check(matches(isDisplayed()))

        // Check category
        onView(withText(item.category)).check(matches(isDisplayed()))

        // Check type
        onView(withText(item.type)).check(matches(isDisplayed()))

        // Check main colour
        if (item.mainColour != null) {
            onView(withText(item.mainColour)).check(matches(isDisplayed()))
        }

        // Check image is displayed (only visible in the list for large screens)
        if (item.imageUrl != null && isLargeScreen) {
            onView(withId(R.id.item_image))
                    .check(matches(AllOf.allOf<View>(
                            CustomMatchers.hasDrawable(),
                            isDisplayed())))
        }
    }

    private fun stubResultFromSelectingImagePicker() {
        val resultData = Intent()
        val fakeImageFile = FakeImageFileImpl()
        fakeImageFile.create(mContext, "fake_image", ".jpg")
        val selectedImageUri = fakeImageFile.getUri(mContext)
        resultData.data = selectedImageUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
    }
}