package com.michaelvescovo.android.itemreaper.about;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.michaelvescovo.android.itemreaper.R;

/**
 * @author Michael Vescovo
 */

public class AboutActivity extends AppCompatActivity implements AboutFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Create the View
        AboutFragment aboutFragment = (AboutFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (aboutFragment == null) {
            aboutFragment = AboutFragment.newInstance();
            initFragment(aboutFragment);
        }
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, fragment);
        transaction.commit();
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
    public void onDialogResumed() {
        // Nothing to do here.
    }

    @Override
    public void onDialogDismissed() {
        // Nothing to do here.
    }
}
