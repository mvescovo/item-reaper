package com.michaelvescovo.android.itemreaper.items;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.about.AboutActivity;
import com.michaelvescovo.android.itemreaper.data.FakeDataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.michaelvescovo.android.itemreaper.data.FakeDataSource.ITEM_1;

/**
 * @author Michael Vescovo
 */

@RunWith(Parameterized.class)
@LargeTest
public class ItemsScreenTest {

    @Rule
    public IntentsTestRule<ItemsActivity> mActivityRule =
            new IntentsTestRule<ItemsActivity>(ItemsActivity.class) {

                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    ((ItemReaperApplication) InstrumentationRegistry.getTargetContext()
                                    .getApplicationContext()).getRepositoryComponent()
                                    .getRepository().deleteAllItems(FakeDataSource.USER_ID);
                }
            };

    @Parameterized.Parameters
    public static Iterable<?> data() {
        return Arrays.asList(
                ITEM_1,
                FakeDataSource.ITEM_2
        );
    }

    private Item mItem;

    public ItemsScreenTest(Item item) {
        mItem = item;
    }

    private boolean mIsLargeScreen;

    @Before
    public void setup() {
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
    }

    @Test
    public void addItemButtonVisible() {
        onView(withId(R.id.add_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAddItemButton_LaunchesAddItemUi() {
        onView(withId(R.id.add_item)).perform(click());
        intended(hasComponent(hasClassName(EditItemActivity.class.getName())));
    }

    @Test
    public void clickAddItemButtonAndThenClickUpButton_NavigatesBackHere() {
        onView(withId(R.id.add_item)).perform(click());
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText(R.string.title_activity_items)).check(matches(isDisplayed()));
    }

    @Test
    public void aboutMenuItemVisible() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_about)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAboutMenuItem_LaunchesAboutUi() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_about)).perform(click());
        if (mIsLargeScreen) {
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
    }

    @Test
    public void SignOutMenuItemVisible() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_sign_out)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSignOutMenuItem_SignsOutAndShowsAuthUi() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.menu_sign_out)).perform(click());
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }

    @Test
    public void noItems_ShowsNoItemsText() {
        onView(withId(R.id.no_items)).check(matches(isDisplayed()));
    }

    @Test
    public void addItem_ShowsItem() {
        // Add item
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.edit_category)).perform(scrollTo()).perform(typeText(mItem.getCategory()));
        onView(withId(R.id.edit_type)).perform(scrollTo()).perform(typeText(mItem.getType()));
        onView(withId(R.id.edit_expiry)).perform(scrollTo()).perform(typeText(mItem.getExpiry()));
        onView(withId(R.id.action_save)).perform(click());

        // Confirm item show in list
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                .scrollTo(hasDescendant(withText(mItem.getCategory()))));
        onView(withText(mItem.getCategory())).check(matches(isDisplayed()));
        onView(withText(mItem.getType())).check(matches(isDisplayed()));
        onView(withText("Expires: " + mItem.getExpiry())).check(matches(isDisplayed()));
    }
}
