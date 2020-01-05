package com.christophenasica.flyscanner.core.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.christophenasica.flyscanner.R;
import com.google.android.gms.maps.MapView;

public class MapInfoView extends RelativeLayout {

    private static final int ID = R.layout.map_info_layout;

    private MapView mMapView;
    private RelativeLayout mExpandableMenu;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mExpandCollapseButton;

    public MapInfoView(Context context) {
        super(context);
        initUI();
    }

    public MapInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MapInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void bindViews() {
        mMapView = findViewById(R.id.mapView);
        mExpandableMenu = findViewById(R.id.expandable_menu);
        mRecyclerView = findViewById(R.id.expandable_menu_recycler);
        mExpandCollapseButton = findViewById(R.id.expandCollapseButton);
    }

    private void initUI() {
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        bindViews();

        //other init
    }

    public void slideMenu(int value) {
        ValueAnimator animator = ValueAnimator.ofInt(mExpandableMenu.getMeasuredHeight(), value);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int val = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams paramsL = mExpandableMenu.getLayoutParams();
                paramsL.height = val;
                mExpandableMenu.setLayoutParams(paramsL);
                mExpandableMenu.requestLayout();
            }
        });
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator(0.85f));
        animator.start();
    }

    public MapView getMapView() {
        return mMapView;
    }

    public RelativeLayout getExpandableMenu() {
        return mExpandableMenu;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public FloatingActionButton getExpandCollapseButton() {
        return mExpandCollapseButton;
    }
}
