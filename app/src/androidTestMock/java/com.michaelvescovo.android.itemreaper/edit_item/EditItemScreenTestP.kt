package com.michaelvescovo.android.itemreaper.edit_item

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import com.michaelvescovo.android.itemreaper.data
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2
import com.michaelvescovo.android.itemreaper.data.Item
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
 * @author Michael Vescovo
 */

@RunWith(Parameterized::class)
@LargeTest
class EditItemScreenTestP(private val mItem: Item) {

    @Rule @JvmField
    var mActivityRule = IntentsTestRule(EditItemActivity::class.java, true, false)

    private var mEspressoHelper: EspressoHelper? = null

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
        val intent = Intent()
        intent.putExtra(EditItemActivity.EXTRA_ITEM_ID, mItem.id)
        mActivityRule.launchActivity(intent)
        mEspressoHelper = EspressoHelper(InstrumentationRegistry.getTargetContext(),
                mActivityRule.activity)

        mEspressoHelper!!.setPortrait()
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
        date {
            correctPurchaseDateShown(mItem.purchaseDate)
            correctExpiryDateShown(mItem.expiry)
        }
        data {
            shop(mItem.shop)
            price(mItem.pricePaid, EDIT_MODE)
            discount(mItem.discount, EDIT_MODE)
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
            showsImage(mItem.imageUrl, EDIT_MODE)
        }
    }
}
