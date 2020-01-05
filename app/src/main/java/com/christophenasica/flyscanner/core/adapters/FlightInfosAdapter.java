package com.christophenasica.flyscanner.core.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.ApplicationManager;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.views.SearchItemView;
import com.christophenasica.flyscanner.data.Flight;

import java.util.List;

public class FlightInfosAdapter extends RecyclerView.Adapter<FlightInfosAdapter.FlightInfosViewHolder> {

    public final List<Flight> mFlightsList;

    public FlightInfosAdapter(List<Flight> flights) {
        mFlightsList = flights;
    }

    @NonNull
    @Override
    public FlightInfosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new FlightInfosViewHolder(new SearchItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull FlightInfosViewHolder viewHolder, int position) {
        SearchItemView view = viewHolder.itemView;
        if (mFlightsList != null && !mFlightsList.isEmpty()) {
            view.setBackgroundColor(position % 2 == 0 ? ContextCompat.getColor(ApplicationManager.getAppContext(), R.color.colorAccent) : Color.WHITE);

            final Flight flight = mFlightsList.get(position);
            if (flight != null) {
                String flightLabel = ApplicationManager.getAppResources().getString(R.string.flight_number_label) + flight.getCallsign();
                view.getFlightName().setText(flightLabel);
                view.getDepAirportName().setText(flight.getAirportDep());
                view.getArrAirportName().setText(flight.getAirportArr());
                view.getDepDate().setText(Utils.timestampToString(flight.getDateDep() * 1000L));
                view.getArrDate().setText(Utils.timestampToString(flight.getDateArr() * 1000L));
                view.getDepHour().setText(Utils.timestampToHourMinute(flight.getDateDep() * 1000L));
                view.getArrHour().setText(Utils.timestampToHourMinute(flight.getDateArr() * 1000L));
                view.getFlightTime().setText(Utils.formatFlightDuration(flight.getFlightTime()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFlightsList.size();
    }

    public static class FlightInfosViewHolder extends RecyclerView.ViewHolder {
        public SearchItemView itemView;

        public FlightInfosViewHolder(@NonNull SearchItemView view) {
            super(view);
            itemView = view;
        }
    }
}
