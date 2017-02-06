package com.michaelvescovo.android.itemreaper.items;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

public class ItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
