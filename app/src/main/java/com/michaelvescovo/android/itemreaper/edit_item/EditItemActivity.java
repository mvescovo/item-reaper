package com.michaelvescovo.android.itemreaper.edit_item;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

/**
 * @author Michael Vescovo
 */

public class EditItemActivity extends AppCompatActivity
        implements EditItemFragment.Callback {

    public static final String EXTRA_ITEM_ID = "com.michaelvescovo.android.itemreaper.item_id";

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
                .applicationComponent(((ItemReaperApplication) getApplication())
                        .getApplicationComponent())
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
    public void configureSupportActionBar(Toolbar toolbar, Drawable icon) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(icon);
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
