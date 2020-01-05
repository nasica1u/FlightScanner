package com.christophenasica.flyscanner.core.fragments;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
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

    private List<Flight> mFlightsList;

    public SearchResultFragment() {}

    public static SearchResultFragment newResultFragment(List<Flight> flights) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FLIGHTS_PARAM, (ArrayList<Flight>) flights);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFlightsList = getArguments().getParcelableArrayList(FLIGHTS_PARAM);
        }
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

        return v;
    }
}
