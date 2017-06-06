package com.michaelvescovo.android.itemreaper.items

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.michaelvescovo.android.itemreaper.ItemReaperApplication
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID
import com.michaelvescovo.android.itemreaper.edit_item.editStaticData
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
class ItemsScreenTest() {

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
    fun staticDataVisible() {
        itemsStaticData {
            title(R.string.title_activity_items)
            sortMenuOption()
            searchMenuOption()
            aboutMenuOption()
            signOutMenuOption()
            editItemButton()
        }
    }

    @Test
    fun clickEditItemButton_LaunchesAddItemUi() {
        items {
            clickEditItemButton(mIsLargeScreen)
            addItemUiLaunched()
            deleteItem()
        }
    }

        @Test
    fun clickEditItemButtonAndThenClickUpButton_NavigatesBackHere() {
        items {
            clickEditItemButton(mIsLargeScreen)
            clickUpNavButton()
            showsItemsFeatureUi()
        }
    }

    @Test
    fun clickAboutMenuItem_LaunchesAboutUi() {
        items {
            clickAboutMenu()
            showsAboutFeatureUi(mIsLargeScreen)
        }
    }

    @Test
    fun clickAboutMenuItemAndThenClickUpButton_NavigatesBackHere() {
        items {
            clickAboutMenu()
            clickUpNavButton()
            showsItemsFeatureUi()
        }
    }

    @Test
    fun clickSignOutMenuItem_SignsOutAndShowsAuthUi() {
        items {
            clickSignOutMenu()
            showsAuthFeatureUi()
        }
    }

    @Test
    fun noItems_ShowsNoItemsText() {
        items {
            showsNoItemsText()
        }
    }

    @Test
    fun clickEditItemThenRotate_takePhotoMenuOptionVisible() {
        if (mIsLargeScreen) {
            items {
                clickEditItemButton(mIsLargeScreen)
            }
            mEspressoHelper!!.rotateScreen()
            editStaticData {
                takePhotoMenuOption()
            }
        }
    }

    @Test
    fun clickEditItemThenRotate_selectImageMenuOptionVisible() {
        if (mIsLargeScreen) {
            items {
                clickEditItemButton(mIsLargeScreen)
            }
            mEspressoHelper!!.rotateScreen()
            editStaticData {
                selectImageMenuOption()
            }
        }
    }

    @Test
    fun clickEditItemThenRotateAndPressUp_AboutMenuItemVisible() {
        if (mIsLargeScreen) {
            items {
                clickEditItemButton(mIsLargeScreen)
            }
            mEspressoHelper!!.rotateScreen()
            items {
                clickUpNavButton()
            }
            itemsStaticData {
                aboutMenuOption()
            }
        }
    }

    @Test
    fun clickAboutMenuItemThenRotateAndPressUp_AboutMenuItemVisible() {
        if (mIsLargeScreen) {
            items {
                clickAboutMenu()
            }
            mEspressoHelper!!.rotateScreen()
            items {
                clickUpNavButton()
            }
            itemsStaticData {
                aboutMenuOption()
            }
        }
    }
}
