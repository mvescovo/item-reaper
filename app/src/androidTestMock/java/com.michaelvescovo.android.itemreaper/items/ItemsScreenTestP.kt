package com.michaelvescovo.android.itemreaper.items

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import com.michaelvescovo.android.itemreaper.ItemReaperApplication
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.*
import com.michaelvescovo.android.itemreaper.data.Item
import com.michaelvescovo.android.itemreaper.util.EspressoHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized::class)
@LargeTest
class ItemsScreenTestP(private val mItem: Item) {

    @Rule @JvmField
    var mActivityRule: IntentsTestRule<ItemsActivity>
            = object : IntentsTestRule<ItemsActivity>(ItemsActivity::class.java) {
        override fun beforeActivityLaunched() {
            (InstrumentationRegistry.getTargetContext()
                    .applicationContext as ItemReaperApplication).repositoryComponent
                    .repository.deleteAllItems(USER_ID)
            super.beforeActivityLaunched()
        }
    }

    private var mEspressoHelper: EspressoHelper? = null
    private var mIsLargeScreen: Boolean = false

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<*> {
            return Arrays.asList(
                    ITEM_1,
                    ITEM_2
            )
        }
    }

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
        items {
            addItem(mItem)
            showsItemInList(mItem, mIsLargeScreen)
        }
    }
}
