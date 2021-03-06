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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.michaelvescovo.android.itemreaper.BuildConfig;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.util.EspressoIdlingResource;
import com.michaelvescovo.android.itemreaper.widget.ItemWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Michael Vescovo
 */

public class AuthFragment extends Fragment implements AuthContract.View,
        GoogleApiClient.OnConnectionFailedListener {

    private AuthContract.Presenter mPresenter;

    @VisibleForTesting
    static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount mGoogleSignInAccount;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

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

        // Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(BuildConfig.WEB_CLIENT_ID)
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mPresenter.handleFirebaseSignInResult(true);
                }
            }
        };
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
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
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
        EspressoIdlingResource.increment();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            EspressoIdlingResource.decrement();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mGoogleSignInAccount = result.getSignInAccount();
            if (mGoogleSignInAccount != null) {
                mPresenter.handleGoogleSignInResult(result.isSuccess());
            }
        } else {
            mGoogleSignInAccount = null;
            mPresenter.handleGoogleSignInResult(result.isSuccess());
        }
    }

    @Override
    public void showFailMessage() {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getString(R.string.auth_failed), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFireBaseAuthUi() {
        EspressoIdlingResource.increment();
        AuthCredential credential = GoogleAuthProvider.getCredential(mGoogleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        EspressoIdlingResource.decrement();
                        // onAuthStateChanged is called at this point
                    }
                });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
