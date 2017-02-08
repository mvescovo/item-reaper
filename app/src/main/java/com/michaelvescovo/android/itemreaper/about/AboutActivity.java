package com.michaelvescovo.android.itemreaper.about;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.R;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.regex.Pattern.compile;

public class AboutActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Typeface appbarTitle = Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf");
        mAppbarTitle.setTypeface(appbarTitle);

        Pattern pattern = compile("");
        String scheme = "";
        Linkify.addLinks(mOpenSourceStatement, pattern, scheme);
        Linkify.addLinks(mForkStatement, pattern, scheme);
        Linkify.addLinks(mReaperIconAttribution, pattern, scheme);
    }
}
