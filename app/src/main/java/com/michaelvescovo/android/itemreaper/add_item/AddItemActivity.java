package com.michaelvescovo.android.itemreaper.add_item;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.michaelvescovo.android.itemreaper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddItemActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_title)
    TextView mAppbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        Typeface appbarTitle = Typeface.createFromAsset(getAssets(), "Nosifer-Regular.ttf");
        mAppbarTitle.setTypeface(appbarTitle);
    }
}
