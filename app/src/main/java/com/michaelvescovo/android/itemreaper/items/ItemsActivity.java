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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
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
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;


/**
 * @author Michael Vescovo
 */

public class ItemsActivity extends AppCompatActivity implements ItemsFragment.Callback,
        AboutFragment.Callback, EditItemFragment.Callback, ItemDetailsFragment.Callback {

    private static final String CURRENT_DIALOG_NAME = "current_dialog_name";
    private static final String ABOUT_DIALOG = "about_dialog";
    private static final String EDIT_ITEM_DIALOG = "edit_item_dialog";
    private static final String ITEM_DETAILS_DIALOG = "item_details_dialog";
    private static final String FRAGMENT_ITEMS = "fragment_items";
    public static final int REQUEST_CODE_ITEM_DELETED = 1;
    public static final String EXTRA_DELETED_ITEM = "deleted_item";

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
                .applicationComponent(((ItemReaperApplication) getApplication())
                        .getApplicationComponent())
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build()
                .inject(this);

        // Setup FAB
        mEditItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemsPresenter.openAddItem();
            }
        });

        if (savedInstanceState != null
                && savedInstanceState.getString(CURRENT_DIALOG_NAME) != null) {
            mCurrentDialogName = savedInstanceState.getString(CURRENT_DIALOG_NAME);
        } else {
            mCurrentDialogName = "";
        }

        mDialogResumed = false;

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
            ((ItemsFragment)itemsFragment).searchItem(query);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDialogOpen) {
            getMenuInflater().inflate(R.menu.items_fragment_menu, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
                    ((ItemsFragment)itemsFragment).searchItem(null);
                    return true;
                }
            });

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
        startActivity(intent);
        finish();
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
                    .applicationComponent(((ItemReaperApplication) getApplication())
                            .getApplicationComponent())
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
                    .applicationComponent(((ItemReaperApplication) getApplication())
                            .getApplicationComponent())
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
    public void onItemDeleted(@NonNull Item item) {
        Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
        ((ItemsFragment)itemsFragment).onItemDeleted(item);
        if (mItemDetailsFragment != null) {
            mItemDetailsFragment.dismiss();
        }
    }

    @Override
    public void onDoneEditing() {
        if (mItemDetailsFragment != null) {
            mCurrentDialogName = ITEM_DETAILS_DIALOG;
        }
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ITEM_DELETED) {
            if (data != null) {
                Item item = (Item) data.getSerializableExtra(EXTRA_DELETED_ITEM);
                Fragment itemsFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ITEMS);
                ((ItemsFragment)itemsFragment).onItemDeleted(item);
            }
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
