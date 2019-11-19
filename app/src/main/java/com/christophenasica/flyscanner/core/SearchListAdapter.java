package com.christophenasica.flyscanner.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.core.views.SearchItemView;
import com.christophenasica.flyscanner.data.Flight;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder> {
    private List<Flight> mFlightsList;

    public SearchListAdapter(List<Flight> flights) {
        mFlightsList = flights;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemView view = new SearchItemView(parent.getContext());
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder viewHolder, int position) {
        viewHolder.searchItemView.test.setText(mFlightsList.get(position).getFlightName());
    }

    @Override
    public int getItemCount() {
        return mFlightsList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        public SearchItemView searchItemView;

        public SearchViewHolder(@NonNull SearchItemView view) {
            super(view);
            searchItemView = view;
        }
    }
}