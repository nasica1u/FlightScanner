package com.christophenasica.flyscanner.core;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
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
        SearchItemView view = viewHolder.searchItemView;
        if (mFlightsList != null && !mFlightsList.isEmpty()) {
            view.getSeparator().setVisibility(viewHolder.getAdapterPosition() != mFlightsList.size() - 1 ? View.VISIBLE : View.GONE);
            view.setBackgroundColor(position % 2 == 0 ? ContextCompat.getColor(ApplicationManager.getAppContext(), R.color.colorAccent) : Color.WHITE);

            Flight flight = mFlightsList.get(position);
            if (flight != null) {
                String flightLabel = ApplicationManager.getAppResources().getString(R.string.flight_number_label) + " " + flight.getFlightName();
                view.getFlightName().setText(flightLabel);
                view.getDepAirportName().setText(flight.getAirportDep());
                view.getArrAirportName().setText(flight.getAirportArr());
                view.getFlightTime().setText(Utils.formatFlightDuration(flight.getFlightTime()));
            }
        }
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
