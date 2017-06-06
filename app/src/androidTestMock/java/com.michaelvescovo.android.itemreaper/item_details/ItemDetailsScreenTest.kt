package com.michaelvescovo.android.itemreaper.item_details

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1
import com.michaelvescovo.android.itemreaper.data.Item
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID
import com.michaelvescovo.android.itemreaper.util.EspressoHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class ItemDetailsScreenTest {

    @Rule @JvmField
    var mActivityRule = IntentsTestRule(ItemDetailsActivity::class.java, true, false)

    private var mEspressoHelper: EspressoHelper? = null
    private var mItem: Item = ITEM_1

    @Before
    fun setup() {
        val intent = Intent()
        intent.putExtra(EXTRA_ITEM_ID, mItem.id)
        mActivityRule.launchActivity(intent)
        mEspressoHelper = EspressoHelper(
                InstrumentationRegistry.getTargetContext(), mActivityRule.activity)
    }

    @Before
    fun registerIdlingResource() {
        Espresso.registerIdlingResources(mActivityRule.activity.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(mActivityRule.activity.countingIdlingResource)
    }

    @Test
    fun staticDataVisible() {
        detailsStaticData {
            upNavigation()
            title(R.string.title_activity_item_details)
            expireItemMenuOption()
            editItemMenuOption()
            adMob()
            purchaseDetailsTitle(R.string.purchase_details_title)
            itemDetailsTitle(R.string.item_details_title)
        }
    }
}
