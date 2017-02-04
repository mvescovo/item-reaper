package com.michaelvescovo.android.itemreaper.auth;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.michaelvescovo.android.itemreaper.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Michael Vescovo
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AuthScreenTest {

    private static final String PACKAGE_NAME = "com.michaelvescovo.android.itemreaper";

    @Rule
    public IntentsTestRule<AuthActivity> mActivityRule = new IntentsTestRule<>(
            AuthActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void signInButtonVisible() {
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
    }

    @Test
    public void progressBarNotVisible() {
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void clickSignIn_TriesToSignInWithGoogle() {
        onView(withId(R.id.sign_in_button)).perform(click());
        intended(toPackage("com.google.android.gms"));
    }

    @Test
    public void clickSignIn_SignInFails_ShowsFailMessage() {
        // Create result with no data to simulate a failed login
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                AuthFragment.RC_SIGN_IN, null);

        // Plug the result into the mock response
        intending(toPackage("com.google.android.gms")).respondWith(result);

        // Click to sign in
        onView(withId(R.id.sign_in_button)).perform(click());

        // Confirm error message is displayed
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.auth_failed))).check(matches(isDisplayed()));
    }

    @Test
    public void clickSignIn_SignInFails_ProgressBarNotVisible() {
        // Create result with no data to simulate a failed login
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                AuthFragment.RC_SIGN_IN, null);

        // Plug the result into the mock response
        intending(toPackage("com.google.android.gms")).respondWith(result);

        // Click to sign in
        onView(withId(R.id.sign_in_button)).perform(click());

        // Confirm progress bar hidden
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void clickSignIn_SignInFails_SignInButtonVisible() {
        // Create result with no data to simulate a failed login
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                AuthFragment.RC_SIGN_IN, null);

        // Plug the result into the mock response
        intending(toPackage("com.google.android.gms")).respondWith(result);

        // Click to sign in
        onView(withId(R.id.sign_in_button)).perform(click());

        // Confirm sign in button visible
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSignIn_SignInSucceeds_DoesNotShowFailMessage() {
        // Click to sign in (must run test on device with valid account that can sign in)
        onView(withId(R.id.sign_in_button)).perform(click());

        // Confirm error message is not displayed
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(R.string.auth_failed))).check(doesNotExist());
    }

    @Test
    public void clickSignIn_SignInSucceeds_ShowsItemsUi() {
        // Click to sign in (must run test on device with valid account that can sign in)
        onView(withId(R.id.sign_in_button)).perform(click());

        // Confirm Items Activity is launched
        intended(allOf(
                hasComponent(hasShortClassName(".items.ItemsActivity")),
                toPackage(PACKAGE_NAME)));
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
    }
}
