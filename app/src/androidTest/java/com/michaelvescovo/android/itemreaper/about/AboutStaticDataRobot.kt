package com.michaelvescovo.android.itemreaper.about

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText

/**
 * @author Michael Vescovo
 */

fun aboutStaticData(func: AboutStaticDataRobot.() -> Unit): AboutStaticDataRobot {
    return AboutStaticDataRobot().apply { func() }
}

class AboutStaticDataRobot {

    fun title(title: Int) {
        onView(withText(title)).check(matches(isDisplayed()))
    }

    fun itemReaperTitle(itemReaperTitle: Int) {
        onView(withText(itemReaperTitle)).check(matches(isDisplayed()))
    }

    fun version(version: Int) {
        onView(withText(version)).check(matches(isDisplayed()))
    }

    fun privacyPolicyTitle(privacyPolicyTitle: Int) {
        onView(withText(privacyPolicyTitle)).check(matches(isDisplayed()))
    }

    fun privacyPolicy(privacyPolicy: Int) {
        onView(withText(privacyPolicy)).check(matches(isDisplayed()))
    }

    fun attributionsTitle(attributionsTitle: Int) {
        onView(withText(attributionsTitle)).check(matches(isDisplayed()))
    }

    fun reaperIconAttribution(reaperIconAttribution: Int) {
        onView(withText(reaperIconAttribution)).check(matches(isDisplayed()))
    }

    fun decapitationSoundEffectAttribution(decapitationSoundAttribution: Int) {
        onView(withText(decapitationSoundAttribution)).check(matches(isDisplayed()))
    }
}
