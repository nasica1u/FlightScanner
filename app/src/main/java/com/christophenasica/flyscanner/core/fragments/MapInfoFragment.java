package com.christophenasica.flyscanner.core.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.ApplicationManager;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.adapters.FlightInfosAdapter;
import com.christophenasica.flyscanner.core.viewmodels.MapViewModel;
import com.christophenasica.flyscanner.core.views.MapInfoView;
import com.christophenasica.flyscanner.data.Flight;
import com.christophenasica.flyscanner.data.FlightState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapInfoFragment extends Fragment {

    private static final String TAG = MapInfoFragment.class.getSimpleName();

    private MapViewModel mMapViewModel;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private List<Flight> mFlights = new ArrayList<>();
    private FlightState mFlightState;

    private RecyclerView mRecyclerView;
    private FlightInfosAdapter mAdapter;

    private boolean isExpanded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mMapViewModel.getIsLoadingAircraftDetails().postValue(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final MapInfoView view = new MapInfoView(getContext());

        mRecyclerView = view.getRecyclerView();
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new FlightInfosAdapter(mFlights);
        mRecyclerView.setAdapter(mAdapter);

        mMapViewModel.getFlightsInfoMenu().observe(getViewLifecycleOwner(), new Observer<List<Flight>>() {
            @Override
            public void onChanged(@Nullable List<Flight> flights) {
                if (flights != null) {
                    mFlights.clear();
                    mFlights.addAll(flights);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mMapViewModel.getCurrentFlightState().observe(getViewLifecycleOwner(), new Observer<FlightState>() {
            @Override
            public void onChanged(@Nullable FlightState flightState) {
                mFlightState = flightState;
                if (flightState != null) {
                    if (mFlightState.getStates() == null) {
                        Toast.makeText(getContext(), R.string.flight_no_direct_info, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updateDirectInfos(view); // Map update
                }
            }
        });

        view.getExpandCollapseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = ApplicationManager.getAppResources().getDimensionPixelOffset(R.dimen.flight_info_menu_expanded_default_height);
                if (getContext() instanceof Activity) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    ((Activity) getContext()).getWindowManager()
                            .getDefaultDisplay()
                            .getMetrics(metrics);
                    height = metrics.heightPixels / 2;
                }
                view.slideMenu(isExpanded ? 0 : height);
                isExpanded = !isExpanded;
            }
        });

        mMapView = view.getMapView();
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (mMapViewModel.getIsDirectInfosUpToDate().getValue() != null && !mMapViewModel.getIsDirectInfosUpToDate().getValue()) {
                            updateDirectInfos(view); // Map update
                        }
                    }
                });
            }
        });

        return view;
    }

    private void updateDirectInfos(MapInfoView view) {
        if (mGoogleMap != null && mFlightState != null && mFlightState.getStates() != null) {
            String[] states = mFlightState.getStates().get(0);
            LatLng latLng = new LatLng(Float.parseFloat(states[FlightState.LATITUDE]), Float.parseFloat(states[FlightState.LONGITUDE]));
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane)).title(states[FlightState.CALLSIGN]));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            mMapViewModel.getIsDirectInfosUpToDate().postValue(true);

            //View Infos update
            view.getFlightName().setText(getString(R.string.flight_direct_name, states[FlightState.CALLSIGN]));
            view.getFlightAltitude().setText(Html.fromHtml(getString(R.string.flight_direct_altitude, states[FlightState.ALTITUDE])));
            view.getFlightSpeed().setText(Html.fromHtml(getString(R.string.flight_direct_speed, Utils.msStringToKmh(states[FlightState.VELOCITY])+"")));
            float verticalSpeed = 0;
            try {
                verticalSpeed = Float.parseFloat(states[FlightState.VERTICAL_RATE]);
            }
            catch (NumberFormatException e) { Log.e(TAG, e.getMessage()); }
            view.getFlightState().setText(Html.fromHtml(getString(R.string.flight_direct_state, verticalSpeed > 0 ? getString(R.string.flight_climb_txt) : getString(R.string.flight_descends_txt))));

            view.getInfoScrollView().setVisibility(View.VISIBLE);
            view.getInfoShadow().setVisibility(View.VISIBLE);
        }
        else {
            mMapViewModel.getIsDirectInfosUpToDate().postValue(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapViewModel.getHasFailedLoadingDirectInfos().postValue(false);
    }
}
