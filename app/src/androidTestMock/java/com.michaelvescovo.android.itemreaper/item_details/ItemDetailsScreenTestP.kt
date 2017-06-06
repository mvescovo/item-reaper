package com.michaelvescovo.android.itemreaper.item_details

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import com.michaelvescovo.android.itemreaper.data.FakeDataSource
import com.michaelvescovo.android.itemreaper.data.Item
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID
import com.michaelvescovo.android.itemreaper.image
import com.michaelvescovo.android.itemreaper.util.EspressoHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

/**
 * Created by Michael Vescovo.
 */

@RunWith(Parameterized::class)
@LargeTest
class ItemDetailsScreenTestP(private val mItem: Item) {

    @Rule @JvmField
    var mActivityRule = IntentsTestRule(ItemDetailsActivity::class.java, true, false)

    private var mEspressoHelper: EspressoHelper? = null

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<*> {
            return Arrays.asList(
                    FakeDataSource.ITEM_1,
                    FakeDataSource.ITEM_2
            )
        }
    }

    @Before
    fun setup() {
        val intent = Intent()
        intent.putExtra(EXTRA_ITEM_ID, mItem.id)
        mActivityRule.launchActivity(intent)
        mEspressoHelper = EspressoHelper(InstrumentationRegistry.getTargetContext(),
                mActivityRule.activity)

        mEspressoHelper!!.setPortrait()
        Espresso.closeSoftKeyboard()
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
    fun showsItemData() {
        com.michaelvescovo.android.itemreaper.data {
            purchaseDate(mItem.purchaseDate)
            shop(mItem.shop)
            price(mItem.pricePaid, DETAILS_MODE)
            discount(mItem.discount, DETAILS_MODE)
            expiryDate(mItem.expiry)
            category(mItem.category)
            subCategory(mItem.subCategory)
            type(mItem.type)
            subType(mItem.subType)
            subType2(mItem.subType2)
            subType3(mItem.subType3)
            mainColour(mItem.mainColour)
            mainColourShade(mItem.mainColourShade)
            accentColour(mItem.accentColour)
            size(mItem.size)
            brand(mItem.brand)
            description(mItem.description)
            note(mItem.note)
        }
        image {
            showsImage(mItem.imageUrl, DETAILS_MODE)
        }
    }
}