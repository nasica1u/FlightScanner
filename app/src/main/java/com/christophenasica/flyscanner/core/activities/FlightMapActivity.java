package com.christophenasica.flyscanner.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.fragments.MapFragment;
import com.christophenasica.flyscanner.data.Flight;

public class FlightMapActivity extends BaseNavbarActivity {

    private static final int ID = R.layout.activity_flight_map;
    public static final String FLIGHT_PARAM = "flight";

    private Flight mFlight;

    public static void startActivity(Activity from, Flight flight) {
        if (flight != null) {
            Intent intent = new Intent(from, FlightMapActivity.class);
            intent.putExtra(FLIGHT_PARAM, flight);
            from.startActivity(intent);
        }
        else {
            Toast.makeText(from, from.getString(R.string.flight_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init NavBar button
        getNavBarLeftButton().setImageResource(R.drawable.ic_arrow_back_white);
        getNavBarLeftButton().setVisibility(View.VISIBLE);
        getNavBarLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mFlight = getIntent().getParcelableExtra(FLIGHT_PARAM);

        initBaseFragment();
    }

    private void initBaseFragment() {
        if (mFlight != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.mapFragmentContainer, MapFragment.newMapFragment(mFlight)).commit();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return ID;
    }
}
