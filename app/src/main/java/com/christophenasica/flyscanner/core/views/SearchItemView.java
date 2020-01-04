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
    private TextView mDepDate;
    private TextView mArrDate;
    private TextView mDepHour;
    private TextView mArrHour;

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
        mDepDate = findViewById(R.id.itemTVDepDate);
        mArrDate = findViewById(R.id.itemTVArrDate);
        mDepHour = findViewById(R.id.itemTVDepHour);
        mArrHour = findViewById(R.id.itemTVArrHour);

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

    public TextView getDepHour() {
        return mDepHour;
    }

    public TextView getArrHour() {
        return mArrHour;
    }

    public TextView getFlightTime() {
        return mFlightTime;
    }

    public TextView getDepDate() {
        return mDepDate;
    }

    public TextView getArrDate() {
        return mArrDate;
    }

    public View getSeparator() {
        return mSeparator;
    }
}
