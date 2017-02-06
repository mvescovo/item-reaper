package com.michaelvescovo.android.itemreaper.items;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import butterknife.ButterKnife;

public class ItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
