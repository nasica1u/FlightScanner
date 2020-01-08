package com.christophenasica.flyscanner.core.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.viewmodels.MainViewModel;
import com.christophenasica.flyscanner.core.viewmodels.Repository;
import com.christophenasica.flyscanner.core.activities.FlightMapActivity;
import com.christophenasica.flyscanner.core.adapters.SearchListAdapter;
import com.christophenasica.flyscanner.data.Flight;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {
    private static final int ID = R.layout.search_result_fragment_layout;

    public static final String FLIGHTS_PARAM = "flights";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Flight> mFlightsList = new ArrayList<>();

    private MainViewModel mMainViewModel;

    public SearchResultFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(ID, container, false);

        mRecyclerView = v.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SearchListAdapter(mFlightsList);
        mRecyclerView.setAdapter(mAdapter);

        Repository.getInstance().getCurrentFlight().observe(this, new Observer<Flight>() {
            @Override
            public void onChanged(@Nullable Flight flight) {
            if (flight != null) {
                FlightMapActivity.startActivity(getActivity(), flight);
            }
            }
        });

        mMainViewModel.getCurrentFlights().observe(this, new Observer<List<Flight>>() {
            @Override
            public void onChanged(@Nullable List<Flight> flights) {
                if (flights != null) {
                    mFlightsList.clear();
                    mFlightsList.addAll(flights);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        return v;
    }
}
