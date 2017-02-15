package com.michaelvescovo.android.itemreaper.items;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.about.AboutActivity;
import com.michaelvescovo.android.itemreaper.about.AboutFragment;
import com.michaelvescovo.android.itemreaper.auth.AuthActivity;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class ItemsActivity extends AppCompatActivity implements ItemsFragment.Callback,
        AboutFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.edit_item)
    FloatingActionButton mEditItemButton;

    @Inject
    ItemsPresenter mItemsPresenter;
    private boolean mIsLargeLayout;
    private boolean mDialogOpen;

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
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDialogOpen) {
            getMenuInflater().inflate(R.menu.items_fragment_menu, menu);
        }
        return true;
    }

    @Override
    public void onAboutSelected() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        if (mIsLargeLayout) {
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
    public void onEditItemSelected() {
        Intent intent = new Intent(this, EditItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void configureSupportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        mDialogOpen = true;
        invalidateOptionsMenu();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
