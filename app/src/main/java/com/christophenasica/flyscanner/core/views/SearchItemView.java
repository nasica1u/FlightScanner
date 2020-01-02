package com.christophenasica.flyscanner.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.christophenasica.flyscanner.R;

public class SearchItemView extends RelativeLayout {

    private static final int ID = R.layout.search_item_view;

    private TextView mFlightName;
    private TextView mDepAirportName;
    private TextView mArrAirportName;
    private TextView mFlightTime;

    private View mSeparator;

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
        mFlightName = findViewById(R.id.flight_name);
        mDepAirportName = findViewById(R.id.itemTVDep);
        mArrAirportName = findViewById(R.id.itemTVArr);
        mFlightTime = findViewById(R.id.flightTime);

        mSeparator = findViewById(R.id.listSeparator);
    }

    private void initUI() {
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        bindViews();

        //other init
    }

    public TextView getFlightName() {
        return mFlightName;
    }

    public TextView getDepAirportName() {
        return mDepAirportName;
    }

    public TextView getArrAirportName() {
        return mArrAirportName;
    }

    public TextView getFlightTime() {
        return mFlightTime;
    }

    public View getSeparator() {
        return mSeparator;
    }
}
