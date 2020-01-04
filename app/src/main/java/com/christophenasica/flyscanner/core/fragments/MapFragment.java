package com.christophenasica.flyscanner.core.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.activities.FlightMapActivity;
import com.christophenasica.flyscanner.core.viewmodels.MapViewModel;
import com.christophenasica.flyscanner.core.views.MapItemView;
import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;
import com.christophenasica.flyscanner.data.FlightPath;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    private MapViewModel mMapViewModel;

    private Flight mFlight;
    private FlightPath mFlightPath;
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private boolean mShowDetails = true;

    public static MapFragment newMapFragment(Flight flight) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(FlightMapActivity.FLIGHT_PARAM, flight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFlight = getArguments().getParcelable(FlightMapActivity.FLIGHT_PARAM);
        }

        mMapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final MapItemView mapItemView = new MapItemView(getContext());

        mMapViewModel.getIsLoadingAircraftDetails().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    mapItemView.getShowDetails().setEnabled(mFlight != null && !isLoading);
                }
            }
        });

        mMapViewModel.getCurrentFlightPath().observe(this, new Observer<FlightPath>() {
            @Override
            public void onChanged(@Nullable FlightPath flightPath) {
                mFlightPath = flightPath;
                updateAircraftPath();
            }
        });

        mapItemView.getShowDetails().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowDetails = !mShowDetails;
                mMapViewModel.getIsLoadingAircraftDetails().postValue(true);
            }
        });

        final Airport depAirport = Utils.getAirportByIcao(mFlight.getAirportDep());
        final Airport arrAirport = Utils.getAirportByIcao(mFlight.getAirportArr());

        mMapView = mapItemView.getMapView();
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                final List<Marker> markers = new ArrayList<>();

                if (depAirport != null && arrAirport != null) {
                    LatLng dep = new LatLng(Double.parseDouble(depAirport.getLat()), Double.parseDouble(depAirport.getLon()));
                    LatLng arr = new LatLng(Double.parseDouble(arrAirport.getLat()), Double.parseDouble(arrAirport.getLon()));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(dep).title(depAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_take_off))));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(arr).title(arrAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_landing))));
                }
                else if (depAirport != null) {
                    LatLng dep = new LatLng(Double.parseDouble(depAirport.getLat()), Double.parseDouble(depAirport.getLon()));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(dep).title(depAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_take_off))));
                }
                else if (arrAirport != null) {
                    LatLng arr = new LatLng(Double.parseDouble(arrAirport.getLat()), Double.parseDouble(arrAirport.getLon()));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(arr).title(arrAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_landing))));
                }

                mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (depAirport != null && arrAirport != null) { // Not zooming if one airport is null
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (Marker marker : markers) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                            mGoogleMap.animateCamera(cameraUpdate);

                            if (mMapViewModel.getIsAircraftPathUpToDate().getValue() != null && mMapViewModel.getIsAircraftPathUpToDate().getValue()) {
                                updateAircraftPath();
                            }
                        }
                    }
                });
            }
        });

        return mapItemView;
    }

    private void updateAircraftPath() {
        if (mGoogleMap != null && mFlightPath != null) {
            ArrayList<String[]> path = mFlightPath.getPath();
            List<LatLng> pathLatLng = new ArrayList<>();
            for (String[] p : path) {
                try {
                    LatLng latLng = new LatLng(Float.parseFloat(p[1]), Float.parseFloat(p[2]));
                    pathLatLng.add(latLng);
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            List<PatternItem> patternItems = Arrays.asList(new Dot(), new Gap(10), new Dash(30), new Gap(10));
            mGoogleMap.addPolyline(new PolylineOptions().addAll(pathLatLng).color(Color.DKGRAY).pattern(patternItems));
            mMapViewModel.getIsAircraftPathUpToDate().postValue(true);
        }
        else {
            mMapViewModel.getIsAircraftPathUpToDate().postValue(false);
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
    }
}
