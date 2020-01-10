package com.christophenasica.flyscanner.core.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.ApplicationManager;
import com.christophenasica.flyscanner.core.RequestManager;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.viewmodels.MainViewModel;
import com.christophenasica.flyscanner.core.views.SearchItemView;
import com.christophenasica.flyscanner.data.Flight;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder> {
    private List<Flight> mFlightsList;
    private MainViewModel mMainViewModel;

    public SearchListAdapter(List<Flight> flights, MainViewModel mainViewModel) {
        mFlightsList = flights;
        mMainViewModel = mainViewModel;
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
            view.setBackgroundColor(position % 2 == 0 ? ContextCompat.getColor(ApplicationManager.getAppContext(), R.color.colorAccent) : Color.WHITE); // alternate cell background color for distinction

            final Flight flight = mFlightsList.get(position);
            if (flight != null) {
                String flightLabel = ApplicationManager.getAppResources().getString(R.string.flight_number_label) + flight.getCallsign();
                view.getFlightName().setText(flightLabel);
                view.getDepAirportName().setText(flight.getAirportDep());
                view.getArrAirportName().setText(flight.getAirportArr());
                view.getDepDate().setText(Utils.timestampToString(flight.getDateDep()*1000L));
                view.getArrDate().setText(Utils.timestampToString(flight.getDateArr()*1000L));
                view.getDepHour().setText(Utils.timestampToHourMinute(flight.getDateDep()*1000L));
                view.getArrHour().setText(Utils.timestampToHourMinute(flight.getDateArr()*1000L));
                view.getFlightTime().setText(Utils.formatFlightDuration(flight.getFlightTime()));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Launching tracking info request : flight Path
                        RequestManager.RequestInfos requestInfos = RequestManager.RequestInfos.initTracksInfos(flight.getFlightName(), flight.getDateArr());
                        RequestManager.getInstance().doGetRequestOnFlights(RequestManager.RequestType.TRACKS, requestInfos);
                        mMainViewModel.getCurrentFlight().postValue(flight); // used to retrieve clicked Flight item
                    }
                });
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
