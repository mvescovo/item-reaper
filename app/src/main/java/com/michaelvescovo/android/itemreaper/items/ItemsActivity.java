package com.michaelvescovo.android.itemreaper.items;

import android.content.Intent;
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
import com.michaelvescovo.android.itemreaper.about.AboutActivity;
import com.michaelvescovo.android.itemreaper.auth.AuthActivity;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsActivity extends AppCompatActivity implements ItemsFragment.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ButterKnife.bind(this);
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

        // Create the Presenter which does the following:
        // Sets itemsFragment as the View for itemsPresenter.
        // Sets itemsPresenter as the presenter for itemsFragment.
        ItemsComponent itemsComponent = DaggerItemsComponent.builder()
                .itemsModule(new ItemsModule(itemsFragment))
                .applicationComponent(((ItemReaperApplication)getApplication()).getApplicationComponent())
                .repositoryComponent(((ItemReaperApplication)getApplication()).getRepositoryComponent())
                .build();
        itemsComponent.getItemsPresenter();
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_fragment_menu, menu);
        return true;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    public void onAboutSelected() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignOutSelected() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
