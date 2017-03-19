package com.michaelvescovo.android.itemreaper.itemDetails;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.michaelvescovo.android.itemreaper.itemDetails.ItemDetailsActivity.EXTRA_ITEM;

/**
 * @author Michael Vescovo
 */

public class ItemDetailsFragment extends AppCompatDialogFragment implements ItemDetailsContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private ItemDetailsContract.Presenter mPresenter;
    private Callback mCallback;
    private boolean mLargeScreen;
    private Typeface mAppbarTypeface;
    private Item mItem;
    private String mImageUrl;
    private MediaPlayer mMediaPlayer;

    public ItemDetailsFragment() {
    }

    public static ItemDetailsFragment newInstance() {
        return new ItemDetailsFragment();
    }

    @Override
    public void setPresenter(@NonNull ItemDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_details, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar, ContextCompat.getDrawable(getContext(),
                R.drawable.ic_close_white_24dp));
        mAppbarTitle.setTypeface(mAppbarTypeface);

        mLargeScreen = getResources().getBoolean(R.bool.large_layout);
        if (mLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if (getArguments().getSerializable(EXTRA_ITEM) != null) {
                mItem = (Item) getArguments().getSerializable(EXTRA_ITEM);
            }
        }

        configureViews();

        return root;
    }

    private void configureViews() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_expire_item:
                playExpireItemSoundEffect();
                mPresenter.expireItem(mItem);
                break;
            case R.id.action_edit_item:
                mPresenter.openEditItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEditItemUi() {
        mCallback.onEditItemSelected(mItem.getId());
    }

    @Override
    public void showItemExpiredMessage(int resourceId, int duration, @Nullable final Item item) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, resourceId, duration);
        if (item != null) {
            snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.unexpireItem(item);
                }
            });
        }
        snackbar.show();
    }

    private void playExpireItemSoundEffect() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.decapitation);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mMediaPlayer.reset();
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
        mAppbarTypeface = Typeface.createFromAsset(getActivity().getAssets(),
                "Nosifer-Regular.ttf");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface Callback {

        void configureSupportActionBar(Toolbar toolbar, Drawable icon);

        void onEditItemSelected(@Nullable String itemId);
    }
}
