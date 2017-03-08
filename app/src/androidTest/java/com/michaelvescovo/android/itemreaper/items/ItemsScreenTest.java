package com.michaelvescovo.android.itemreaper.items;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.about.AboutActivity;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.util.FakeImageFileImpl;
import com.michaelvescovo.android.itemreaper.util.RotationHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_2;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.USER_ID;
import static com.michaelvescovo.android.itemreaper.matcher.ImageViewHasDrawableMatcher.hasDrawable;
import static org.hamcrest.core.AllOf.allOf;

/**
 * @author Michael Vescovo
 */

@RunWith(Enclosed.class)
@LargeTest
public class ItemsScreenTest {

    /*
    * Only run these tests once.
    * */
    public static class StandardItemsScreenTest {
        @Rule
        public IntentsTestRule<ItemsActivity> mActivityRule =
                new IntentsTestRule<ItemsActivity>(ItemsActivity.class) {
                    @Override
                    protected void beforeActivityLaunched() {
                        super.beforeActivityLaunched();
                        ((ItemReaperApplication) InstrumentationRegistry.getTargetContext()
                                .getApplicationContext()).getRepositoryComponent()
                                .getRepository().deleteAllItems(USER_ID);
                    }
                };
        private RotationHelper mRotationHelper;
        private boolean mIsLargeScreen;

        @Before
        public void setup() {
            mRotationHelper = new RotationHelper(InstrumentationRegistry.getTargetContext(),
                    mActivityRule.getActivity());
            mIsLargeScreen = mActivityRule.getActivity().getResources().getBoolean(R.bool.large_layout);
        }

        @Before
        public void registerIdlingResource() {
            Espresso.registerIdlingResources(
                    mActivityRule.getActivity().getCountingIdlingResource());
        }

        @After
        public void unregisterIdlingResource() {
            Espresso.unregisterIdlingResources(
                    mActivityRule.getActivity().getCountingIdlingResource());
        }

        @Test
        public void titleVisible() {
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
        }

        @Test
        public void editItemButtonVisible() {
            onView(withId(R.id.edit_item)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withId(R.id.edit_item)).check(matches(isDisplayed()));
        }

        @Test
        public void clickEditItemButton_LaunchesAddItemUi() {
            onView(withId(R.id.edit_item)).perform(click());
            if (!mIsLargeScreen) {
                intended(hasComponent(hasClassName(EditItemActivity.class.getName())));
            }
            onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.title_activity_edit_item)).check(matches(isDisplayed()));
        }

        @Test
        public void clickEditItemButtonAndThenClickUpButton_NavigatesBackHere() {
            onView(withId(R.id.edit_item)).perform(click());
            onView(withContentDescription("Navigate up")).perform(click());
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
        }

        @Test
        public void aboutMenuItemVisible() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_about)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.menu_about)).check(matches(isDisplayed()));
        }

        @Test
        public void clickAboutMenuItem_LaunchesAboutUi() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_about)).perform(click());
            if (mIsLargeScreen) {
                onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
                mRotationHelper.rotateScreen();
                onView(withText(R.string.title_activity_about)).check(matches(isDisplayed()));
            } else {
                intended(hasComponent(hasClassName(AboutActivity.class.getName())));
            }
        }

        @Test
        public void clickAboutMenuItemAndThenClickUpButton_NavigatesBackHere() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_about)).perform(click());
            onView(withContentDescription("Navigate up")).perform(click());
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
        }

        @Test
        public void SignOutMenuItemVisible() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_sign_out)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.menu_sign_out)).check(matches(isDisplayed()));
        }

        @Test
        public void clickSignOutMenuItem_SignsOutAndShowsAuthUi() {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(R.string.menu_sign_out)).perform(click());
            onView(withText(R.string.app_name)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withText(R.string.app_name)).check(matches(isDisplayed()));
        }

        @Test
        public void noItems_ShowsNoItemsText() {
            onView(withId(R.id.no_items)).check(matches(isDisplayed()));
            mRotationHelper.rotateScreen();
            onView(withId(R.id.no_items)).check(matches(isDisplayed()));
        }
    }

    /*
    * Run these tests for each parameter.
    * */
    @RunWith(Parameterized.class)
    public static class ParameterisedItemsScreenTest {

        @Rule
        public IntentsTestRule<ItemsActivity> mActivityRule =
                new IntentsTestRule<ItemsActivity>(ItemsActivity.class) {
                    @Override
                    protected void beforeActivityLaunched() {
                        super.beforeActivityLaunched();
                        ((ItemReaperApplication) InstrumentationRegistry.getTargetContext()
                                .getApplicationContext()).getRepositoryComponent()
                                .getRepository().deleteAllItems(USER_ID);
                    }
                };
        private RotationHelper mRotationHelper;
        private Item mItem;
        private boolean mIsLargeScreen;

        public ParameterisedItemsScreenTest(Item item) {
            mItem = item;
        }

        @Parameterized.Parameters
        public static Iterable<?> data() {
            return Arrays.asList(
                    ITEM_1,
                    ITEM_2
            );
        }

        @Before
        public void setup() {
            mRotationHelper = new RotationHelper(InstrumentationRegistry.getTargetContext(),
                    mActivityRule.getActivity());
            mIsLargeScreen = mActivityRule.getActivity().getResources().getBoolean(R.bool.large_layout);
        }

        @Before
        public void registerIdlingResource() {
            Espresso.registerIdlingResources(
                    mActivityRule.getActivity().getCountingIdlingResource());
        }

        @After
        public void unregisterIdlingResource() {
            Espresso.unregisterIdlingResources(
                    mActivityRule.getActivity().getCountingIdlingResource());
        }

        @Test
        public void addItem_ShowsItemInList() {
        /*
        * Add item
        * */

            // Click to add item
            onView(withId(R.id.edit_item)).perform(click());

            // Type price paid
            if (mItem.getPricePaid() != -1) {
                onView(withId(R.id.edit_price_paid)).perform(scrollTo())
                        .perform(typeText(String.valueOf(mItem.getPricePaid())), closeSoftKeyboard());
            }

            // Type expiry
            Calendar expiry = Calendar.getInstance();
            expiry.setTimeInMillis(mItem.getExpiry());
            int expiryDay = expiry.get(Calendar.DAY_OF_MONTH);
            int expiryMonth = expiry.get(Calendar.MONTH);
            expiryMonth++; // Java months start at 0.
            int expiryYear = expiry.get(Calendar.YEAR);
            onView(withId(R.id.edit_expiry_date_day)).perform(scrollTo())
                    .perform(typeText(String.valueOf(expiryDay)), closeSoftKeyboard());
            onView(withId(R.id.edit_expiry_date_month)).perform(scrollTo())
                    .perform(typeText(String.valueOf(expiryMonth)), closeSoftKeyboard());
            onView(withId(R.id.edit_expiry_date_year)).perform(scrollTo())
                    .perform(typeText(String.valueOf(expiryYear)), closeSoftKeyboard());

            // Type category
            //noinspection ConstantConditions
            onView(withId(R.id.edit_category)).perform(scrollTo())
                    .perform(typeText(mItem.getCategory()), closeSoftKeyboard());

            // Type type
            //noinspection ConstantConditions
            onView(withId(R.id.edit_type)).perform(scrollTo())
                    .perform(typeText(mItem.getType()), closeSoftKeyboard());

            // Type primary colour
            if (mItem.getPrimaryColour() != null) {
                onView(withId(R.id.edit_primary_colour)).perform(scrollTo())
                        .perform(typeText(mItem.getPrimaryColour()), closeSoftKeyboard());
            }

            // Add image
            if (mItem.getImageUrl() != null) {
                stubResultFromSelectingImagePicker();
                onView(withId(R.id.action_select_image)).perform(click());
            }

        /*
        * Confirm item shows in list
        * */
            // Navigate back to the list
            onView(withContentDescription("Navigate up")).perform(click());
            confirmItemInList(expiry);
            mRotationHelper.rotateScreen();
            confirmItemInList(expiry);
        }

        private void confirmItemInList(Calendar expiry) {
            // Scroll to item
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .scrollTo(hasDescendant(withText(mItem.getCategory()))));

            // Check price paid
            if (mItem.getPricePaid() != -1) {
                onView(withText("Paid: $" + mItem.getPricePaid())).check(matches(isDisplayed()));
            }

            // Check expiry
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
            if (mIsLargeScreen) {
                onView(withText(simpleDateFormat.format(expiry.getTime())))
                        .check(matches(isDisplayed()));
            } else {
                onView(withText("Expires: " + simpleDateFormat.format(expiry.getTime())))
                        .check(matches(isDisplayed()));
            }

            // Check category
            onView(withText(mItem.getCategory())).check(matches(isDisplayed()));

            // Check type
            onView(withText(mItem.getType())).check(matches(isDisplayed()));

            // Check primary colour
            if (mItem.getPrimaryColour() != null) {
                onView(withText(mItem.getPrimaryColour())).check(matches(isDisplayed()));
            }

            // Check image is displayed
            if (mItem.getImageUrl() != null && mIsLargeScreen) {
                onView(withId(R.id.item_image))
                        .check(matches(allOf(
                                hasDrawable(),
                                isDisplayed())));
            }
        }

        private void stubResultFromSelectingImagePicker() {
            Intent resultData = new Intent();
            FakeImageFileImpl fakeImageFile = new FakeImageFileImpl();
            fakeImageFile.create(mActivityRule.getActivity(), "fake_image", ".jpg");
            Uri selectedImageUri = fakeImageFile.getUri();
            resultData.setData(selectedImageUri);
            Instrumentation.ActivityResult result =
                    new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
            intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result);
        }
    }
}

