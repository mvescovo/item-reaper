package com.michaelvescovo.android.itemreaper.about;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.R;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.regex.Pattern.compile;

/**
 * @author Michael Vescovo
 */

public class AboutFragment extends AppCompatDialogFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;
    @BindView(R.id.open_source_statement)
    TextView mOpenSourceStatement;
    @BindView(R.id.fork_statement)
    TextView mForkStatement;
    @BindView(R.id.reaper_icon_attribution)
    TextView mReaperIconAttribution;

    private Callback mCallback;
    private Typeface mAppbarTypeface;

    public AboutFragment() {}

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AboutFragment.Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement callback");
        }
        mAppbarTypeface = Typeface.createFromAsset(getActivity().getAssets(),
                "Nosifer-Regular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, root);
        mCallback.configureSupportActionBar(mToolbar);
        boolean isLargeScreen = getResources().getBoolean(R.bool.large_layout);
        if (isLargeScreen) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        mAppbarTitle.setTypeface(mAppbarTypeface);
        Pattern pattern = compile("");
        String scheme = "";
        Linkify.addLinks(mOpenSourceStatement, pattern, scheme);
        Linkify.addLinks(mForkStatement, pattern, scheme);
        Linkify.addLinks(mReaperIconAttribution, pattern, scheme);
        return root;
    }

    public interface Callback {

        void configureSupportActionBar(Toolbar toolbar);
    }
}
