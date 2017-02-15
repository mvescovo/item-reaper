package com.michaelvescovo.android.itemreaper.edit_item;

import android.graphics.Typeface;
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
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class EditItemActivity extends AppCompatActivity
        implements EditItemFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        Typeface appbarTitle = Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf");
        mAppbarTitle.setTypeface(appbarTitle);

        // Create the View
        EditItemFragment editItemFragment = (EditItemFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (editItemFragment == null) {
            editItemFragment = EditItemFragment.newInstance();
            initFragment(editItemFragment);
        }

        // Create the Presenter which does the following:
        // Sets itemsFragment as the View for itemsPresenter.
        // Sets itemsPresenter as the presenter for itemsFragment.
        EditItemComponent editItemComponent = DaggerEditItemComponent.builder()
                .editItemModule(new EditItemModule(editItemFragment))
                .applicationComponent(((ItemReaperApplication)getApplication())
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

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
