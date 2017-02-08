package com.michaelvescovo.android.itemreaper.auth;

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
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Typeface appbarTitle = Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf");
        mAppbarTitle.setTypeface(appbarTitle);

        // Create the View
        AuthFragment authFragment = (AuthFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (authFragment == null) {
            authFragment = AuthFragment.newInstance();
            initFragment(authFragment);
        }

        // Create the Presenter which does the following:
        // Sets authFragment as the View for authPresenter.
        // Sets authPresenter as the presenter for authFragment.
        AuthComponent authComponent = DaggerAuthComponent.builder()
                .authModule(new AuthModule(authFragment))
                .applicationComponent(((ItemReaperApplication)getApplication()).getApplicationComponent())
                .build();
        authComponent.getAuthPresenter();
    }

    private void initFragment(Fragment authFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, authFragment);
        transaction.commit();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
