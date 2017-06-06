package com.michaelvescovo.android.itemreaper.edit_item

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.image
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
class EditItemScreenTest {

    @Rule @JvmField
    var mActivityRule = IntentsTestRule(EditItemActivity::class.java)

    private var mEspressoHelper: EspressoHelper? = null

    @Before
    fun setup() {
        mEspressoHelper = EspressoHelper(
                InstrumentationRegistry.getTargetContext(), mActivityRule.activity)

        Espresso.registerIdlingResources(
                mActivityRule.activity.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Espresso.unregisterIdlingResources(
                mActivityRule.activity.countingIdlingResource)
    }

    @Test
    fun staticDataVisible() {
        staticData {
            upNavigation()
            title(R.string.title_activity_edit_item)
            takePhotoMenuOption()
            selectImageMenuOption()
            deleteItemMenuOption()
            purchaseDetailsTitle(R.string.edit_purchase_details_title)
            purchaseDateTitle(R.string.edit_purchase_date_title)
            purchaseDateSpinner(
                    R.string.edit_date_today,
                    R.string.edit_date_yesterday,
                    R.string.edit_date_custom,
                    R.string.edit_date_unknown
            )
            shopHint(R.string.edit_shop_hint)
            pricePaidHint(R.string.edit_price_paid_hint)
            discountHint(R.string.edit_discount_hint)
            itemDetailsTitle(R.string.edit_item_details_title)
            expiryDateTitle(R.string.edit_expiry_date_title)
            expiryDateSpinner(
                    R.string.edit_date_today,
                    R.string.edit_date_yesterday,
                    R.string.edit_date_custom,
                    R.string.edit_date_unknown
            )
            categoryHint(R.string.edit_category_hint)
            subCategoryHint(R.string.edit_sub_category_hint)
            typeHint(R.string.edit_type_hint)
            subTypeHint(R.string.edit_sub_type_hint)
            subType2Hint(R.string.edit_sub_type2_hint)
            subType3Hint(R.string.edit_sub_type3_hint)
            mainColourHint(R.string.edit_main_colour_hint)
            mainColourShadeHint(R.string.edit_main_colour_shade_hint)
            accentColourHint(R.string.edit_accent_colour_hint)
            sizeHint(R.string.edit_size_hint)
            brandHint(R.string.edit_brand_hint)
            descriptionHint(R.string.edit_description_hint)
            noteHint(R.string.edit_note_hint)
        }
    }

    @Test
    fun selectDate_CorrectDateSelected() {
        date {
            selectPurchaseDateToday()
            purchaseDateTodaySelected()

            selectPurchaseDateYesterday()
            purchaseDateYesterdaySelected()

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            purchaseDateCustomSelected("01/Jan/17")

            selectPurchaseDateUnknown()
            purchaseDateUnknownSelected()

            selectExpiryDateToday()
            expiryDateTodaySelected()

            selectExpiryDateYesterday()
            expiryDateYesterdaySelected()

            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            expiryDateCustomSelected("01/Jan/17")

            selectExpiryDateUnknown()
            expiryDateUnknownSelected()
        }
    }

    @Test
    fun rotationRetainsDates() {
        date {
            selectPurchaseDateToday()
            selectExpiryDateToday()
            mEspressoHelper!!.rotateScreen()
            purchaseDateTodaySelected()
            expiryDateTodaySelected()

            selectPurchaseDateYesterday()
            selectExpiryDateYesterday()
            mEspressoHelper!!.rotateScreen()
            purchaseDateYesterdaySelected()
            expiryDateYesterdaySelected()

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            mEspressoHelper!!.rotateScreen()
            purchaseDateCustomSelected("01/Jan/17")
            expiryDateCustomSelected("01/Jan/17")

            selectPurchaseDateUnknown()
            selectExpiryDateUnknown()
            mEspressoHelper!!.rotateScreen()
            purchaseDateUnknownSelected()
            expiryDateUnknownSelected()
        }
    }

    @Test
    fun selectCustomDate_ClickCancel_PreviousSelectionDisplayed() {
        date {
            selectPurchaseDateToday()
            selectExpiryDateToday()
            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = false)
            enterCustomExpiryDate("01/Jan/17", selectCustomDate = false)
            purchaseDateTodaySelected()
            expiryDateTodaySelected()

            selectPurchaseDateYesterday()
            selectExpiryDateYesterday()
            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = false)
            enterCustomExpiryDate("01/Jan/17", selectCustomDate = false)
            purchaseDateYesterdaySelected()
            expiryDateYesterdaySelected()

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            enterCustomPurchaseDate("02/Jan/17", selectCustomDate = false)
            enterCustomExpiryDate("02/Jan/17", selectCustomDate = false)
            purchaseDateCustomSelected("01/Jan/17")
            expiryDateCustomSelected("01/Jan/17")

            selectPurchaseDateUnknown()
            selectExpiryDateUnknown()
            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = false)
            enterCustomExpiryDate("01/Jan/17", selectCustomDate = false)
            purchaseDateUnknownSelected()
            expiryDateUnknownSelected()
        }
    }

    @Test
    fun selectCustomDate_SelectDifferentOption_CustomDateRemovedFromSpinner() {
        date {
            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            selectPurchaseDateToday()
            customPurchaseDateNotInSpinner("01/Jan/17")

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            selectPurchaseDateYesterday()
            customPurchaseDateNotInSpinner("01/Jan/17")

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            enterCustomPurchaseDate("02/Jan/17", selectCustomDate = true)
            customPurchaseDateNotInSpinner("01/Jan/17")

            enterCustomPurchaseDate("01/Jan/17", selectCustomDate = true)
            selectPurchaseDateUnknown()
            customPurchaseDateNotInSpinner("01/Jan/17")

            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            selectExpiryDateToday()
            customExpiryDateNotInSpinner("01/Jan/17")

            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            selectExpiryDateYesterday()
            customExpiryDateNotInSpinner("01/Jan/17")

            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            enterCustomExpiryDate("02/Jan/17", selectCustomDate = true)
            customExpiryDateNotInSpinner("01/Jan/17")

            enterCustomExpiryDate("01/Jan/17", selectCustomDate = true)
            selectExpiryDateUnknown()
            customExpiryDateNotInSpinner("01/Jan/17")
        }
    }

    @Test
    fun takePicture_ShowsPicture() {
        image {
            takePicture()
            showsImage()
            mEspressoHelper!!.rotateScreen()
            showsImage()
        }
    }

    @Test
    fun selectImage_ShowsSelectedImage() {
        image {
            selectImage()
            showsImage()
            mEspressoHelper!!.rotateScreen()
            showsImage()
        }
    }

    @Test
    fun removeImage_ImageRemoved() {
        image {
            selectImage()
            showsImage()
            removeImage()
            imageNotShown(EDIT_MODE)
        }
    }
}

