package com.michaelvescovo.android.itemreaper.settings

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class SettingsScreenTest {

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(SettingsActivity::class.java)

    @Test
    fun staticDataVisible() {
        settingsStaticData {
            title("Settings")
        }
    }
}
