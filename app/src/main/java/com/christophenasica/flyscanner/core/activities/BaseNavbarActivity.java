package com.christophenasica.flyscanner.core.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.christophenasica.flyscanner.R;

public abstract class BaseNavbarActivity extends BaseActivity {

    private static final int ID = R.layout.base_navbar_activity_layout;

    protected RelativeLayout mMainContent;
    protected ImageButton mNavBarLeftButton;
    protected ImageButton mNavBarRightButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(ID);
        mMainContent = findViewById(R.id.mainContent);
        mNavBarLeftButton = findViewById(R.id.nbLeftButton);
        mNavBarRightButton = findViewById(R.id.nbRightButton);
        View.inflate(this, getContentLayoutId(), mMainContent);
    }

    public RelativeLayout getMainContent() {
        return mMainContent;
    }

    public ImageButton getNavBarLeftButton() {
        return mNavBarLeftButton;
    }

    public ImageButton getNavBarRightButton() {
        return mNavBarRightButton;
    }

    protected abstract int getContentLayoutId();
}
