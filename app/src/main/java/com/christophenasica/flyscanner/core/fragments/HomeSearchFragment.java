package com.christophenasica.flyscanner.core.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.AirportManager;
import com.christophenasica.flyscanner.core.views.FlightFormView;

import java.util.Calendar;

public class HomeSearchFragment extends Fragment {

    private final Calendar mDeparturePickerCalendar = Calendar.getInstance();
    private final Calendar mArrivalPickerCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FlightFormView rootView = new FlightFormView(getContext());

        rootView.updateDateLabel(rootView.getFromPickerEditText(), Calendar.getInstance().getTime());
        rootView.updateDateLabel(rootView.getToPickerEditText(), Calendar.getInstance().getTime());

        final DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDeparturePickerCalendar.set(Calendar.YEAR, year);
                mDeparturePickerCalendar.set(Calendar.MONTH, month);
                mDeparturePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                rootView.updateDateLabel(rootView.getFromPickerEditText(), mDeparturePickerCalendar.getTime());
            }
        };

        final DatePickerDialog.OnDateSetListener arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mArrivalPickerCalendar.set(Calendar.YEAR, year);
                mArrivalPickerCalendar.set(Calendar.MONTH, month);
                mArrivalPickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                rootView.updateDateLabel(rootView.getToPickerEditText(), mArrivalPickerCalendar.getTime());
            }
        };

        rootView.getFromPickerEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), departureDateSetListener, mDeparturePickerCalendar.get(Calendar.YEAR), mDeparturePickerCalendar.get(Calendar.MONTH), mDeparturePickerCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        rootView.getToPickerEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), arrivalDateSetListener, mArrivalPickerCalendar.get(Calendar.YEAR), mArrivalPickerCalendar.get(Calendar.MONTH), mArrivalPickerCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        Spinner airportSelector = rootView.getDropdown();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, AirportManager.getInstance().getAirportNameList());
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        airportSelector.setAdapter(spinnerAdapter);

        return rootView;
    }
}
