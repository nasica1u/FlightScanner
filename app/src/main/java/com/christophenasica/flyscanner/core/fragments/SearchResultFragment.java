package com.christophenasica.flyscanner.core.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.RequestManager;
import com.christophenasica.flyscanner.core.adapters.SearchListAdapter;
import com.christophenasica.flyscanner.core.viewmodels.MainViewModel;
import com.christophenasica.flyscanner.core.viewmodels.Repository;
import com.christophenasica.flyscanner.data.Flight;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment {
    private static final int ID = R.layout.search_result_fragment_layout;

    private RecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;
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

        mAdapter = new SearchListAdapter(mFlightsList, mMainViewModel);
        mRecyclerView.setAdapter(mAdapter);

        RequestManager.RequestAsyncTask.cancelRunningTasks(); // reset all running AsyncTask to prevent invalid datas
        Repository.getInstance().getCurrentFlightPath().postValue(null); // reset current navigation path
        Repository.getInstance().getCurrentFlightState().postValue(null); // reset current navigation state

        mMainViewModel.getCurrentFlight().observe(getViewLifecycleOwner(), new Observer<Flight>() {
            @Override
            public void onChanged(@Nullable Flight flight) {
            if (flight != null) {
                if (getActivity() != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.searchFragmentContainer, MapFragment.newMapFragment(flight)).addToBackStack(null).commit();
                    mMainViewModel.getCurrentFlight().postValue(null); // reset value to navigate back on previous fragment
                }
            }
            }
        });

        mMainViewModel.getCurrentFlights().observe(getViewLifecycleOwner(), new Observer<List<Flight>>() {
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
