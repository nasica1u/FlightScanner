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
import com.christophenasica.flyscanner.core.RequestManager;
import com.christophenasica.flyscanner.core.Utils;
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
import java.util.Calendar;
import java.util.List;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    public static final String FLIGHT_PARAM = "flight";

    private MapViewModel mMapViewModel;

    private Flight mFlight;
    private FlightPath mFlightPath;
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private boolean mShowDetails = true;



    public static MapFragment newMapFragment(Flight flight) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(FLIGHT_PARAM, flight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFlight = getArguments().getParcelable(FLIGHT_PARAM);
        }

        mMapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final MapItemView mapItemView = new MapItemView(getContext());

        mMapViewModel.getIsAircraftPathUpToDate().postValue(false);

        mMapViewModel.getIsLoadingAircraftDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                mapItemView.getShowDetails().setEnabled(mFlight != null && !isLoading);
            }
            }
        });

        mMapViewModel.getCurrentFlightPath().observe(getViewLifecycleOwner(), new Observer<FlightPath>() {
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
                if (getActivity() != null && mFlight != null) {
                    // Launch direct info map screen
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.searchFragmentContainer, new MapInfoFragment()).addToBackStack(null).commit();
                    // Prepare and launch states request for direct infos
                    RequestManager.RequestInfos requestInfos = RequestManager.RequestInfos.initStatesInfos(mFlight.getFlightName());
                    RequestManager.getInstance().doGetRequestOnFlights(RequestManager.RequestType.STATES, requestInfos);

                    // Launch request to show list of flights for the aircraft (history)
                    Calendar today = Calendar.getInstance();
                    Calendar threeDaysAgo = Calendar.getInstance();
                    int end = (int) (today.getTimeInMillis() / 1000);
                    threeDaysAgo.add(Calendar.DAY_OF_WEEK, -3); // taking last 3 days period
                    int begin = (int) (threeDaysAgo.getTimeInMillis() / 1000);
                    // Launch history request
                    RequestManager.RequestInfos requestInfos2 = RequestManager.RequestInfos.initHistoryInfos(mFlight.getFlightName(), begin, end);
                    RequestManager.getInstance().doGetRequestOnFlights(RequestManager.RequestType.FLIGHTS_BY_AIRCRAFT, requestInfos2);
                }
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

                if (depAirport != null) {
                    LatLng dep = new LatLng(Double.parseDouble(depAirport.getLat()), Double.parseDouble(depAirport.getLon()));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(dep).title(depAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_take_off)))); // add departure airport marker on map
                }
                if (arrAirport != null) {
                    LatLng arr = new LatLng(Double.parseDouble(arrAirport.getLat()), Double.parseDouble(arrAirport.getLon()));
                    markers.add(googleMap.addMarker(new MarkerOptions().position(arr).title(arrAirport.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane_landing)))); // add arrival airport marker on map
                }

                updateAircraftPath(); // to draw back the path if you navigate through fragments

                mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (depAirport != null && arrAirport != null) { // Not zooming if one airport is null
                            if (markers.size() > 0) {
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (Marker marker : markers) {
                                    builder.include(marker.getPosition());
                                }
                                LatLngBounds bounds = builder.build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                                mGoogleMap.animateCamera(cameraUpdate);
                            }
                            updateAircraftPath(); // if map is loaded and has received no path update before
                        }
                    }
                });
            }
        });

        return mapItemView;
    }

    private void updateAircraftPath() {
        if (mMapViewModel.getIsAircraftPathUpToDate().getValue() != null && !mMapViewModel.getIsAircraftPathUpToDate().getValue()) { // if needs to be updated only
            if (mGoogleMap != null && mFlightPath != null) {
                List<String[]> path = mFlightPath.getPath();
                List<LatLng> pathLatLng = new ArrayList<>();
                for (String[] p : path) {
                    try {
                        LatLng latLng = new LatLng(Float.parseFloat(p[1]), Float.parseFloat(p[2])); // create LatLng instance per path point
                        pathLatLng.add(latLng);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                List<PatternItem> patternItems = Arrays.asList(new Dot(), new Gap(10), new Dash(30), new Gap(10));
                mGoogleMap.addPolyline(new PolylineOptions().addAll(pathLatLng).color(Color.DKGRAY).pattern(patternItems)); // convert points into a polyline
                mMapViewModel.getIsAircraftPathUpToDate().postValue(true); // declare path as up to date
            } else {
                mMapViewModel.getIsAircraftPathUpToDate().postValue(false); // declare path as not updated yet
            }
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
