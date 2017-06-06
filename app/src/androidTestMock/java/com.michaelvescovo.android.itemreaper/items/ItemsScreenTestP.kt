package com.michaelvescovo.android.itemreaper.items

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.PickerActions.setDate
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.DatePicker
import com.michaelvescovo.android.itemreaper.ItemReaperApplication
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID
import com.michaelvescovo.android.itemreaper.data.Item
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers.hasDrawable
import com.michaelvescovo.android.itemreaper.util.EspressoHelper
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl
import com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized::class)
@LargeTest
class ItemsScreenTestP(private val mItem: Item) {

    @Rule
    var mActivityRule: IntentsTestRule<ItemsActivity> = object : IntentsTestRule<ItemsActivity>(ItemsActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            (InstrumentationRegistry.getTargetContext()
                    .applicationContext as ItemReaperApplication).repositoryComponent
                    .repository.deleteAllItems(USER_ID)
        }
    }
    private val mContext = InstrumentationRegistry.getTargetContext()
    private var mEspressoHelper: EspressoHelper? = null
    private var mIsLargeScreen: Boolean = false

    @Before
    fun setup() {
        mEspressoHelper = EspressoHelper(InstrumentationRegistry.getTargetContext(),
                mActivityRule.activity)
        mIsLargeScreen = mActivityRule.activity.resources.getBoolean(R.bool.large_layout)
    }

    @Before
    fun registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.activity.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mActivityRule.activity.countingIdlingResource)
    }

    @Test
    fun addItem_ShowsItemInList() {
        /*
        * Add item
        * */

        // Click to add item
        onView(withId(R.id.edit_item)).perform(click())
        Espresso.closeSoftKeyboard()

        // Type editPrice paid
        if (mItem.pricePaid != -1) {
            val priceString = getPriceFromTotalCents(mItem.pricePaid)
            onView(withId(R.id.edit_price_paid)).perform(scrollTo())
                    .perform(typeText(priceString), closeSoftKeyboard())
        }

        // Select expiry
        val expiry = Calendar.getInstance()
        expiry.timeInMillis = mItem.expiry
        onView(withId(R.id.expiry_date_spinner)).perform(scrollTo()).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)),
                `is`(mContext.getString(R.string.edit_date_custom))))
                .inRoot(isPlatformPopup())
                .perform(click())
        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(
                expiry.get(Calendar.YEAR),
                expiry.get(Calendar.MONTH) + 1, // Months start at 0.
                expiry.get(Calendar.DAY_OF_MONTH)))
        onView(withId(android.R.id.button1)).perform(click())

        // Type category

        onView(withId(R.id.edit_category)).perform(scrollTo())
                .perform(typeText(mItem.category!!), closeSoftKeyboard())

        // Type type

        onView(withId(R.id.edit_type)).perform(scrollTo())
                .perform(typeText(mItem.type!!), closeSoftKeyboard())

        // Type main colour
        if (mItem.mainColour != null) {
            onView(withId(R.id.edit_main_colour)).perform(scrollTo())
                    .perform(typeText(mItem.mainColour!!), closeSoftKeyboard())
        }

        // Add image
        if (mItem.imageUrl != null) {
            stubResultFromSelectingImagePicker()
            onView(withId(R.id.action_select_image)).perform(click())
        }

        /*
        * Confirm item shows in list
        * */
        // Navigate back to the list
        onView(withContentDescription("Navigate up")).perform(click())
        confirmItemInList(expiry)
        mEspressoHelper!!.rotateScreen()
        confirmItemInList(expiry)
    }

    private fun confirmItemInList(expiry: Calendar) {
        // Scroll to item
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                .scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(mItem.category))))

        // Check editPrice paid
        if (mItem.pricePaid != -1) {
            val priceString = getPriceFromTotalCents(mItem.pricePaid)
            onView(withText("Paid: $$priceString")).check(matches(isDisplayed()))
        }

        // Check expiry
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yy", Locale.ENGLISH)
        if (mIsLargeScreen) {
            onView(withText(simpleDateFormat.format(expiry.time)))
                    .check(matches(isDisplayed()))
        }

        // Check category
        onView(withText(mItem.category)).check(matches(isDisplayed()))

        // Check type
        onView(withText(mItem.type)).check(matches(isDisplayed()))

        // Check main colour
        if (mItem.mainColour != null) {
            onView(withText(mItem.mainColour)).check(matches(isDisplayed()))
        }

        // Check image is displayed
        if (mItem.imageUrl != null && mIsLargeScreen) {
            onView(withId(R.id.item_image))
                    .check(matches(allOf<View>(
                            hasDrawable(),
                            isDisplayed())))
        }
    }

    private fun stubResultFromSelectingImagePicker() {
        val resultData = Intent()
        val fakeImageFile = FakeImageFileImpl()
        fakeImageFile.create(mActivityRule.activity, "fake_image", ".jpg")
        val selectedImageUri = fakeImageFile.getUri(mActivityRule.activity)
        resultData.data = selectedImageUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
    }

    companion object {

        @Parameterized.Parameters
        fun data(): Iterable<*> {
            return Arrays.asList(
                    ITEM_1
                    //                ,ITEM_2
            )
        }
    }
}
