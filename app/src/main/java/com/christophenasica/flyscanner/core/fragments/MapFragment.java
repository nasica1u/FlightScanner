package com.christophenasica.flyscanner.core.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.activities.FlightMapActivity;
import com.christophenasica.flyscanner.core.views.MapItemView;
import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private Flight mFlight;
    private MapView mMapView;
    private GoogleMap mGoogleMap;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapItemView mapItemView = new MapItemView(getContext());

        mapItemView.getShowDetails().setText(getString(R.string.button_show_details));
        mapItemView.getShowDetails().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    // Adding the path
                    googleMap.addPolyline(new PolylineOptions().clickable(true).add(dep, arr).color(Color.DKGRAY));
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
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                });
            }
        });

        return mapItemView;
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
