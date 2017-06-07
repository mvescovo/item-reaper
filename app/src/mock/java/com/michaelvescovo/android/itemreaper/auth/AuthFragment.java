package com.michaelvescovo.android.itemreaper.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.common.SignInButton;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.widget.ItemWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class AuthFragment extends Fragment implements AuthContract.View {

    private AuthContract.Presenter mPresenter;

    @VisibleForTesting
    static final int RC_SIGN_IN = 1;
    @BindView(R.id.sign_in_button)
    SignInButton mSignInButton;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    public AuthFragment() {
    }

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_auth, container, false);
        ButterKnife.bind(this, root);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.googleSignIn();
            }
        });
        return root;
    }

    @Override
    public void setPresenter(@NonNull AuthContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSignInButton(boolean visible) {
        if (visible) {
            mSignInButton.setVisibility(View.VISIBLE);
        } else {
            mSignInButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGoogleSignInUi() {
        mPresenter.handleGoogleSignInResult(true);
    }

    @Override
    public void showFailMessage() {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(R.string.auth_failed), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFireBaseAuthUi() {
        mPresenter.handleFirebaseSignInResult(true);
    }

    @Override
    public void updateWidget() {
        Intent updateWidgetIntent = new Intent(getContext(), ItemWidgetProvider.class);
        updateWidgetIntent.setAction(ItemWidgetProvider.ACTION_DATA_UPDATED);
        getContext().sendBroadcast(updateWidgetIntent);
    }

    @Override
    public void closeAuthUi() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
