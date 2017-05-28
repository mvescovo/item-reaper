package com.michaelvescovo.android.itemreaper.about

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.michaelvescovo.android.itemreaper.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class AboutScreenTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(AboutActivity::class.java)

    @Test
    fun staticDataVisible() {
        aboutStaticData {
            title(R.string.title_activity_about)
            itemReaperTitle(R.string.title_item_reaper)
            version(R.string.version)
            privacyPolicyTitle(R.string.title_privacy_policy)
            privacyPolicy(R.string.privacy_policy)
            attributionsTitle(R.string.title_attributions)
            reaperIconAttribution(R.string.reaper_icon_attribution)
            decapitationSoundEffectAttribution(R.string.decapitation_sound_attribution)
        }
    }
}
