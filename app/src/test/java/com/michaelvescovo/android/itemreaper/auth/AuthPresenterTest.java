package com.michaelvescovo.android.itemreaper.auth;

import com.michaelvescovo.android.itemreaper.SharedPreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * @author Michael Vescovo
 */

public class AuthPresenterTest {

    private AuthPresenter mAuthPresenter;

    @Mock
    AuthContract.View mView;

    @Mock
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private static final String USER_ID = "testUser";

    @Before
    public void setUpPresenter() {
        MockitoAnnotations.initMocks(this);
        mAuthPresenter = new AuthPresenter(mView, mSharedPreferencesHelper);
    }

    @Test
    public void clickSignIn_ShowsProgressBar() {
        mAuthPresenter.googleSignIn();
        verify(mView).setProgressIndicator(true);
    }

    @Test
    public void clickSignIn_HidesSignInButton() {
        mAuthPresenter.googleSignIn();
        verify(mView).showSignInButton(false);
    }

    @Test
    public void clickSignIn_TriesToSignInWithGoogle() {
        mAuthPresenter.googleSignIn();
        verify(mView).showGoogleSignInUi();
    }

    @Test
    public void googleSignInFails_ShowsFailMsg() {
        mAuthPresenter.handleGoogleSignInResult(false, null);
        verify(mView).showFailMessage();
    }

    @Test
    public void googleSignInFails_ShowsSignInButton() {
        mAuthPresenter.handleGoogleSignInResult(false, null);
        verify(mView).showSignInButton(true);
    }

    @Test
    public void googleSignInFails_HidesProgressBar() {
        mAuthPresenter.handleGoogleSignInResult(false, null);
        verify(mView).setProgressIndicator(false);
    }

    @Test
    public void googleSignInSucceeds_SetsUserId() {
        mAuthPresenter.handleGoogleSignInResult(true, USER_ID);
        verify(mSharedPreferencesHelper).saveUserId(USER_ID);
    }

    @Test
    public void googleSignInSucceeds_TriesToSignInWithFirebaseAuth() {
        mAuthPresenter.handleGoogleSignInResult(true, USER_ID);
        verify(mView).showFireBaseAuthUi();
    }

    @Test
    public void firebaseAuthSignInFails_ShowsFailMsg() {
        mAuthPresenter.handleFirebaseSignInResult(false);
        verify(mView).showFailMessage();
    }

    @Test
    public void firebaseAuthSignInFails_ShowsSignInButton() {
        mAuthPresenter.handleFirebaseSignInResult(false);
        verify(mView).showSignInButton(true);
    }

    @Test
    public void firebaseAuthSignInSucceeds_ShowsSignInButton() {
        mAuthPresenter.handleFirebaseSignInResult(true);
        verify(mView).showSignInButton(true);
    }

    @Test
    public void firebaseAuthSignInFails_HidesProgressBar() {
        mAuthPresenter.handleFirebaseSignInResult(false);
        verify(mView).setProgressIndicator(false);
    }

    @Test
    public void firebaseAuthSignInSucceeds_HidesProgressBar() {
        mAuthPresenter.handleFirebaseSignInResult(true);
        verify(mView).setProgressIndicator(false);
    }

    @Test
    public void firebaseAuthSignInSucceeds_ShowsItemsUi() {
        mAuthPresenter.handleFirebaseSignInResult(true);
        verify(mView).showItemsUi();
    }
}
