package com.michaelvescovo.android.itemreaper.itemDetails;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.REQUEST_CODE_ITEM_DELETED;


public class ItemDetailsActivity extends AppCompatActivity implements ItemDetailsFragment.Callback {

    public static final String EXTRA_ITEM = "com.michaelvescovo.android.itemreaper.item";
    private static final String FRAGMENT_ITEM_DETAILS = "fragment_item_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        String itemId = null;
        if (getIntent().getStringExtra(EXTRA_ITEM_ID) != null) {
            itemId = getIntent().getStringExtra(EXTRA_ITEM_ID);
        }

        // Create the View
        ItemDetailsFragment itemDetailsFragment = (ItemDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (itemDetailsFragment == null) {
            itemDetailsFragment = ItemDetailsFragment.newInstance();
            initFragment(itemDetailsFragment);
            if (itemId != null) {
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_ITEM_ID, itemId);
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
        transaction.add(R.id.contentFrame, fragment, FRAGMENT_ITEM_DETAILS);
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
    public void onEditItemSelected(@Nullable String itemId) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        startActivityForResult(intent, REQUEST_CODE_ITEM_DELETED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ITEM_DELETED) {
            if (data != null) {
                setResult(resultCode, data);
                finish();
            }
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
