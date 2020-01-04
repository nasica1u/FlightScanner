package com.christophenasica.flyscanner.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.christophenasica.flyscanner.R;
import com.google.android.gms.maps.MapView;

public class MapItemView extends RelativeLayout {

    private static final int ID = R.layout.map_layout;

    private Button mShowDetails;
    private MapView mMapView;

    public MapItemView(Context context) {
        super(context);
        initUI();
    }

    public MapItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MapItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void bindViews() {
        mShowDetails = findViewById(R.id.showDetails);
        mMapView = findViewById(R.id.mapView);
    }

    private void initUI() {
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        bindViews();

        //other init
    }

    public Button getShowDetails() {
        return mShowDetails;
    }

    public MapView getMapView() {
        return mMapView;
    }
}
