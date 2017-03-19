package com.michaelvescovo.android.itemreaper.itemDetails;

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

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.REQUEST_CODE_ITEM_DELETED;


public class ItemDetailsActivity extends AppCompatActivity implements ItemDetailsFragment.Callback {

    public static final String EXTRA_ITEM = "com.michaelvescovo.android.itemreaper.item";
    public static final String EXTRA_ITEMS_SIZE = "com.michaelvescovo.android.itemreaper.items_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Item item = null;
        if (getIntent().getSerializableExtra(EXTRA_ITEM) != null) {
            item = (Item) getIntent().getSerializableExtra(EXTRA_ITEM);
        }
        int itemsSize = getIntent().getIntExtra(EXTRA_ITEMS_SIZE, -1);

        // Create the View
        ItemDetailsFragment itemDetailsFragment = (ItemDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (itemDetailsFragment == null) {
            itemDetailsFragment = ItemDetailsFragment.newInstance();
            initFragment(itemDetailsFragment);
            if (item != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_ITEM, item);
                bundle.putInt(EXTRA_ITEMS_SIZE, itemsSize);
                itemDetailsFragment.setArguments(bundle);
            }
        }

        // Create the Presenter which does the following:
        // Sets itemDetailsFragment as the View for itemDetailsPresenter.
        // Sets itemDetailsPresenter as the presenter for itemDetailsFragment.
        ItemDetailsComponent itemDetailsComponent = DaggerItemDetailsComponent.builder()
                .itemDetailsModule(new ItemDetailsModule(itemDetailsFragment))
                .applicationComponent(((ItemReaperApplication) getApplication())
                        .getApplicationComponent())
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build();
        itemDetailsComponent.getItemDetailsPresenter();
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details_fragment_menu, menu);
        return true;
    }

    @Override
    public void configureSupportActionBar(Toolbar toolbar, Drawable icon) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onEditItemSelected(@NonNull String itemId) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        startActivityForResult(intent, REQUEST_CODE_ITEM_DELETED);
    }

    @Override
    public void showNoItemsText(boolean active) {
        // Nothing to do here.
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
