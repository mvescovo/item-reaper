package com.michaelvescovo.android.itemreaper.auth;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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
