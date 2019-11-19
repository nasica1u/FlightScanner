package com.christophenasica.flyscanner.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.christophenasica.flyscanner.R;

public class SearchItemView extends RelativeLayout {

    private static final int ID = R.layout.search_item_view;

    public TextView test;

    public SearchItemView(Context context) {
        super(context);
        initUI();
    }

    public SearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SearchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void bindViews() {
        test = findViewById(R.id.test);
    }

    private void initUI() {
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        bindViews();

        //other init
    }
}
