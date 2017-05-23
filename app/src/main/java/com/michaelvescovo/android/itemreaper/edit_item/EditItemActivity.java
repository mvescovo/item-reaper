package com.michaelvescovo.android.itemreaper.edit_item;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.EXTRA_DELETED_ITEM;

/**
 * @author Michael Vescovo
 */

public class EditItemActivity extends AppCompatActivity
        implements EditItemFragment.Callback {

    public static final String EXTRA_ITEM_ID = "com.michaelvescovo.android.itemreaper.item_id";

    MenuItem mTakePhotoMenuItem;
    MenuItem mSelectImageMenuItem;
    MenuItem mDeleteItemMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Create the View
        EditItemFragment editItemFragment = (EditItemFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (editItemFragment == null) {
            editItemFragment = EditItemFragment.newInstance();
            // Add itemId if it exists
            if (getIntent().getStringExtra(EXTRA_ITEM_ID) != null) {
                Bundle args = new Bundle();
                args.putString(EXTRA_ITEM_ID, getIntent().getStringExtra(EXTRA_ITEM_ID));
                editItemFragment.setArguments(args);
            }
            initFragment(editItemFragment);
        }

        // Create the Presenter which does the following:
        // Sets itemsFragment as the View for itemsPresenter.
        // Sets itemsPresenter as the presenter for itemsFragment.
        EditItemComponent editItemComponent = DaggerEditItemComponent.builder()
                .editItemModule(new EditItemModule(editItemFragment))
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build();
        editItemComponent.getEditItemPresenter();
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_fragment_menu, menu);
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
    public void configureSupportActionBar(Toolbar toolbar, Drawable icon) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(icon);
        }
    }

    @Override
    public void onDialogResumed() {
        // Nothing to do here.
    }

    @Override
    public void onItemDeleted(@NonNull Item item) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETED_ITEM, item);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onDoneEditing() {
        // Nothing to do here.
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

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
