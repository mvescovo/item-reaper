package com.michaelvescovo.android.itemreaper.edit_item

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.michaelvescovo.android.itemreaper.R
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers.hasDrawable
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

/**
 * Created by Michael Vescovo.
 */

fun image(func: EditItemImageRobot.() -> Unit): EditItemImageRobot {
    return EditItemImageRobot().apply { func() }
}

class EditItemImageRobot {

    private val mContext: Context = InstrumentationRegistry.getTargetContext()

    fun takePicture() {
        // Stub a result from taking a picture with the camera
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        // Take picture with camera
        onView(withId(R.id.action_take_photo)).perform(click())
    }

    fun selectImage() {
        stubResultFromSelectingImagePicker()
        onView(withId(R.id.action_select_image)).perform(click())
    }

    fun removeImage() {
        onView(withId(R.id.edit_item_remove_image_button)).perform(scrollTo()).perform(click())
    }

    fun imageNotShown() {
        onView(withId(R.id.edit_item_image)).check(matches(not(isDisplayed())))
        onView(withId(R.id.edit_item_remove_image_button)).check(matches(not(isDisplayed())))
    }

    fun showsImage() {
        onView(withId(R.id.edit_item_image)).perform(scrollTo())
                .check(matches(allOf<View>(hasDrawable(), isDisplayed())))
    }

    fun showsImage(imageUrl: String?) {
        if (imageUrl == null) {
            imageNotShown()
        } else {
            showsImage()
        }
    }

    private fun stubResultFromSelectingImagePicker() {
        val resultData = Intent()
        val fakeImageFile = FakeImageFileImpl()
        fakeImageFile.create(mContext, "fake_image", ".jpg")
        val selectedImageUri = fakeImageFile.getUri(mContext)
        resultData.data = selectedImageUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
    }
}