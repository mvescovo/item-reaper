package com.michaelvescovo.android.itemreaper.items;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;
import com.michaelvescovo.android.itemreaper.about.AboutActivity;
import com.michaelvescovo.android.itemreaper.about.AboutFragment;
import com.michaelvescovo.android.itemreaper.auth.AuthActivity;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.edit_item.DaggerEditItemComponent;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemComponent;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemFragment;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemModule;
import com.michaelvescovo.android.itemreaper.item_details.DaggerItemDetailsComponent;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsActivity;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsComponent;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsFragment;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsModule;
import com.michaelvescovo.android.itemreaper.util.Analytics;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.items.SortItemsDialogFragment.EXTRA_SORT_BY;
import static com.michaelvescovo.android.itemreaper.items.SortItemsDialogFragment.SORT_BY_EXPIRY;
import static com.michaelvescovo.android.itemreaper.items.SortItemsDialogFragment.SORT_BY_PURCHASE_DATE;
import static com.michaelvescovo.android.itemreaper.util.Constants.REQUEST_CODE_SIGNIN;
import static com.michaelvescovo.android.itemreaper.widget.ItemWidgetProvider.ACTION_EDIT_NEW_ITEM;


/**
 * @author Michael Vescovo
 */

public class ItemsActivity extends AppCompatActivity implements ItemsFragment.Callback,
        AboutFragment.Callback, EditItemFragment.Callback, ItemDetailsFragment.Callback,
        SortItemsDialogFragment.SortItemsDialogListener {

    public static final String EXTRA_DELETED_ITEM = "deleted_item";
    public static final String EXTRA_EDIT_NEW_ITEM = "edit_new_item";
    public static final int REQUEST_CODE_ITEM_DELETED = 1;
    private static final String STATE_SEARCH_VIEW_OPEN = "search_view_open";
    private static final String STATE_QUERY = "query";
    private static final String CURRENT_DIALOG_NAME = "current_dialog_name";
    private static final String ABOUT_DIALOG = "about_dialog";
    private static final String EDIT_ITEM_DIALOG = "edit_item_dialog";
    private static final String ITEM_DETAILS_DIALOG = "item_details_dialog";
    private static final String FRAGMENT_ITEMS = "fragment_items";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.edit_item)
    FloatingActionButton mEditItemButton;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    MenuItem mTakePhotoMenuItem;
    MenuItem mSelectImageMenuItem;
    MenuItem mDeleteItemMenuItem;

    @Inject
    ItemsPresenter mItemsPresenter;
    private boolean mIsLargeLayout;
    private boolean mDialogOpen;
    private boolean mDialogResumed;
    private String mCurrentDialogName;
    private ItemDetailsFragment mItemDetailsFragment;
    private boolean mSearchViewExpanded;
    private boolean mSearchViewResumed;
    private String mQuery;
    private String mItemId;
    private boolean mEditNewItem;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private int mCurrentSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ButterKnife.bind(this);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        mDialogOpen = false;
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Typeface appbarTitle = Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf");
        mAppbarTitle.setTypeface(appbarTitle);

        // Create the View
        ItemsFragment itemsFragment = (ItemsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (itemsFragment == null) {
            itemsFragment = ItemsFragment.newInstance();
            initFragment(itemsFragment);
        }

        // The Presenter in injected (because it's used in this class) which does the following:
        // Sets itemsFragment as the View for itemsPresenter.
        // Sets itemsPresenter as the presenter for itemsFragment.
        DaggerItemsComponent.builder()
                .itemsModule(new ItemsModule(itemsFragment))
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build()
                .inject(this);

        // Setup FAB
        mEditItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemsPresenter.openAddItem();
                Analytics.logEventAddItem(getApplicationContext());
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(CURRENT_DIALOG_NAME) != null) {
                mCurrentDialogName = savedInstanceState.getString(CURRENT_DIALOG_NAME);
            } else {
                mCurrentDialogName = "";
            }
            mSearchViewExpanded = savedInstanceState.getBoolean(STATE_SEARCH_VIEW_OPEN, false);
            mQuery = savedInstanceState.getString(STATE_QUERY);
        }

        if (getIntent() != null) {
            mItemId = getIntent().getStringExtra(EXTRA_ITEM_ID);
            if (getIntent().getAction() != null
                    && getIntent().getAction().contentEquals(ACTION_EDIT_NEW_ITEM)) {
                mEditNewItem = getIntent().getBooleanExtra(EXTRA_EDIT_NEW_ITEM, false);
            }
        }

        mDialogResumed = false;
        mSearchViewResumed = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mItemId = intent.getStringExtra(EXTRA_ITEM_ID);
        if (intent.getAction() != null
                && intent.getAction().contentEquals(ACTION_EDIT_NEW_ITEM)) {
            mEditNewItem = intent.getBooleanExtra(EXTRA_EDIT_NEW_ITEM, false);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mItemId != null) {
            onItemDetailsSelected(mItemId);
            getIntent().removeExtra(EXTRA_ITEM_ID);
            mItemId = null;
        } else if (mEditNewItem) {
            onEditItemSelected(null);
            getIntent().removeExtra(EXTRA_EDIT_NEW_ITEM);
            mEditNewItem = false;
        }
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment, FRAGMENT_ITEMS);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_DIALOG_NAME, mCurrentDialogName);
        outState.putBoolean(STATE_SEARCH_VIEW_OPEN, mSearchViewExpanded);
        outState.putString(STATE_QUERY, mQuery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDialogOpen) {
            getMenuInflater().inflate(R.menu.items_fragment_menu, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setSubmitButtonEnabled(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (mSearchViewExpanded) {
                        if (!mSearchViewResumed) {
                            // For some reason when the activity is resumed it always thinks the
                            // new text is "" even when I've restored the saved value in mQuery.
                            // By not updating mQuery with this empty string the search can restore
                            // correctly after the user presses back from selecting one of the
                            // searched items.
                            mQuery = newText;
                        }
                        mSearchViewResumed = false;
                        Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
                        ((ItemsFragment) itemsFragment).searchItem(mQuery);
                    }
                    return true;
                }
            });
            MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    mSearchViewExpanded = true;
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    mSearchViewExpanded = false;
                    invalidateOptionsMenu();
                    mQuery = null;
                    Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
                    ((ItemsFragment) itemsFragment).searchItem(mQuery);
                    return true;
                }
            });
            if (mSearchViewExpanded) {
                String tempQuery = mQuery;
                searchMenuItem.expandActionView();
                searchView.setQuery(tempQuery, true);
                mSearchViewResumed = true;
            }
        } else if (mDialogResumed) {
            menu.clear();
            getMenuInflater().inflate(R.menu.items_fragment_menu, menu);
            mDialogResumed = false;
        } else if (mCurrentDialogName.equals(EDIT_ITEM_DIALOG)) {
            getMenuInflater().inflate(R.menu.edit_item_fragment_menu, menu);
        } else if (mCurrentDialogName.equals(ITEM_DETAILS_DIALOG)) {
            getMenuInflater().inflate(R.menu.item_details_fragment_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mTakePhotoMenuItem = menu.findItem(R.id.action_take_photo);
        mSelectImageMenuItem = menu.findItem(R.id.action_select_image);
        mDeleteItemMenuItem = menu.findItem(R.id.action_delete_item);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAboutSelected() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        if (mIsLargeLayout) {
            mCurrentDialogName = ABOUT_DIALOG;
            aboutFragment.show(getSupportFragmentManager(), "dialog");
        } else {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSignOutSelected() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNIN);
    }

    @Override
    public void onEditItemSelected(@Nullable String itemId) {
        if (mIsLargeLayout) {
            EditItemFragment editItemFragment = EditItemFragment.newInstance();
            if (itemId != null) {
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_ITEM_ID, itemId);
                editItemFragment.setArguments(bundle);
            }
            mCurrentDialogName = EDIT_ITEM_DIALOG;

            // Need to create an EditItemsPresenter for when the fragment is run as a dialog
            // from this Activity.
            EditItemComponent editItemComponent = DaggerEditItemComponent.builder()
                    .editItemModule(new EditItemModule(editItemFragment))
                    .repositoryComponent(((ItemReaperApplication) getApplication())
                            .getRepositoryComponent())
                    .build();
            editItemComponent.getEditItemPresenter();

            editItemFragment.show(getSupportFragmentManager(), "dialog");
            editItemFragment.setCancelable(false);
        } else {
            Intent intent = new Intent(this, EditItemActivity.class);
            if (itemId != null) {
                intent.putExtra(EXTRA_ITEM_ID, itemId);
            }
            startActivityForResult(intent, REQUEST_CODE_ITEM_DELETED);
        }
    }

    @Override
    public void onItemDetailsSelected(@NonNull String itemId) {
        if (mIsLargeLayout) {
            mItemDetailsFragment = ItemDetailsFragment.newInstance();
            mCurrentDialogName = ITEM_DETAILS_DIALOG;

            // Need to create an ItemDetailsPresenter for when the fragment is run as a dialog
            // from this Activity.
            ItemDetailsComponent itemDetailsComponent = DaggerItemDetailsComponent.builder()
                    .itemDetailsModule(new ItemDetailsModule(mItemDetailsFragment))
                    .repositoryComponent(((ItemReaperApplication) getApplication())
                            .getRepositoryComponent())
                    .build();
            itemDetailsComponent.getItemDetailsPresenter();
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ITEM_ID, itemId);
            mItemDetailsFragment.setArguments(bundle);

            mItemDetailsFragment.show(getSupportFragmentManager(), "dialog");
        } else {
            Intent intent = new Intent(this, ItemDetailsActivity.class);
            intent.putExtra(EXTRA_ITEM_ID, itemId);
            startActivityForResult(intent, REQUEST_CODE_ITEM_DELETED);
        }
    }

    @Override
    public Snackbar onShowSnackbar(String text, int duration) {
        return Snackbar.make(mCoordinatorLayout, text,
                duration);
    }

    @Override
    public void configureSupportActionBar(Toolbar toolbar, Drawable icon) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(icon);
        }
        mDialogOpen = true;
    }

    @Override
    public void onDialogResumed() {
        // Set the correct menu on the background Activity for when the dialog is dismissed.
        mDialogResumed = true;
        onCreateOptionsMenu(mToolbar.getMenu());
    }

    @Override
    public void onDialogDismissed() {
        mDialogOpen = false;
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        onCreateOptionsMenu(mToolbar.getMenu());
    }

    @Override
    public void onItemDeleted(@NonNull Item item) {
        Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
        ((ItemsFragment) itemsFragment).onItemDeleted(item);
        if (mItemDetailsFragment != null) {
            mItemDetailsFragment.dismiss();
        }
    }

    @Override
    public void onDoneEditing() {
        if (mItemDetailsFragment != null) {
            mCurrentDialogName = ITEM_DETAILS_DIALOG;
        }
        onDialogDismissed();
    }

    @Override
    public void editMenuEnabled(boolean enabled) {
        if (mTakePhotoMenuItem != null) {
            mTakePhotoMenuItem.setEnabled(enabled);
        }
        if (mSelectImageMenuItem != null) {
            mSelectImageMenuItem.setEnabled(enabled);
        }
        if (mDeleteItemMenuItem != null) {
            mDeleteItemMenuItem.setEnabled(enabled);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ITEM_DELETED) {
            if (resultCode == RESULT_OK && data != null) {
                Item item = (Item) data.getSerializableExtra(EXTRA_DELETED_ITEM);
                Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
                ((ItemsFragment) itemsFragment).onItemDeleted(item);
            }
        }
        if (requestCode == REQUEST_CODE_SIGNIN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            } else {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    mItemsPresenter.setUid(firebaseUser.getUid());
                } else {
                    mItemsPresenter.setUid(null);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSortSelected(SharedPreferencesHelper preferences) {
        mSharedPreferencesHelper = preferences;
        mCurrentSort = preferences.getSortBy();
        DialogFragment dialog = new SortItemsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SORT_BY, mCurrentSort);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "SortItemsDialogFragment");
    }

    @Override
    public void onSortByExpirySelected() {
        mSharedPreferencesHelper.saveSortBy(SORT_BY_EXPIRY);
        mCurrentSort = SORT_BY_EXPIRY;
        sort();
    }

    @Override
    public void onSortByPurchaseDateSelected() {
        mSharedPreferencesHelper.saveSortBy(SORT_BY_PURCHASE_DATE);
        mCurrentSort = SORT_BY_PURCHASE_DATE;
        sort();
    }

    private void sort() {
        Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
        ((ItemsFragment) itemsFragment).onSortChanged(mCurrentSort);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
