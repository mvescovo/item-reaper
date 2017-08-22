package com.michaelvescovo.android.itemreaper.settings

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers

/**
 * @author Michael Vescovo
 */

fun settingsStaticData(func: SettingsStaticDataRobot.() -> Unit): SettingsStaticDataRobot {
    return SettingsStaticDataRobot().apply { func() }
}

class SettingsStaticDataRobot {

    fun title(title: String) {
        Espresso.onView(ViewMatchers.withText(title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}