package com.michaelvescovo.android.itemreaper

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.michaelvescovo.android.itemreaper.matcher.CustomMatchers.hasDrawable
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

/**
 * Created by Michael Vescovo.
 */

fun image(func: ImageRobot.() -> Unit): ImageRobot {
    return ImageRobot().apply { func() }
}

class ImageRobot {

    val EDIT_MODE: String = "editMode"
    val DETAILS_MODE: String = "detailsMode"
    private val mContext: Context = InstrumentationRegistry.getTargetContext()

    fun takePicture() {
        Espresso.closeSoftKeyboard()
        // Stub a result from taking a picture with the camera
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        // Take picture with camera
        onView(withId(R.id.action_take_photo)).perform(click())
    }

    fun selectImage() {
        Espresso.closeSoftKeyboard()
        stubResultFromSelectingImagePicker()
        onView(withId(R.id.action_select_image)).perform(click())
    }

    fun removeImage() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.item_remove_image_button)).perform(scrollTo()).perform(click())
    }

    fun imageNotShown(mode: String) {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.item_image)).check(matches(not(isDisplayed())))
        if (mode == EDIT_MODE) {
            onView(withId(R.id.item_remove_image_button)).check(matches(not(isDisplayed())))
        }
    }

    fun showsImage() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.item_image)).perform(scrollTo())
                .check(matches(allOf<View>(hasDrawable(), isDisplayed())))
    }

    fun showsImage(imageUrl: String?, mode: String) {
        Espresso.closeSoftKeyboard()
        if (imageUrl == null) {
            imageNotShown(mode)
        } else {
            showsImage()
        }
    }

    private fun stubResultFromSelectingImagePicker() {
        Espresso.closeSoftKeyboard()
        val resultData = Intent()
        val fakeImageFile = FakeImageFileImpl()
        fakeImageFile.create(mContext, "fake_image", ".jpg")
        val selectedImageUri = fakeImageFile.getUri(mContext)
        resultData.data = selectedImageUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
    }
}