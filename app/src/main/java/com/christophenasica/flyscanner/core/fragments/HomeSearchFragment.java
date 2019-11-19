package com.christophenasica.flyscanner.core.fragments;

import android.app.DatePickerDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.AirportManager;
import com.christophenasica.flyscanner.core.RequestManager;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.activities.SearchResultActivity;
import com.christophenasica.flyscanner.core.views.FlightFormView;
import com.christophenasica.flyscanner.data.Airport;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeSearchFragment extends Fragment implements RequestManager.RequestListener {

    private static final String TAG = HomeSearchFragment.class.getSimpleName();

    private final Calendar mDeparturePickerCalendar = Calendar.getInstance();
    private final Calendar mArrivalPickerCalendar = Calendar.getInstance();

    private final FormInfos mFormInfos = new FormInfos();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FlightFormView rootView = new FlightFormView(getContext());

        if (mFormInfos.icaoFrom == null && mFormInfos.icaoTo == null) {
            String defaultIcao = AirportManager.getInstance().getAirportList().get(0).getIcao();
            mFormInfos.icaoFrom = defaultIcao;
            mFormInfos.icaoTo = defaultIcao;
        }

        rootView.updateDateLabel(rootView.getFromPickerEditText(), Calendar.getInstance().getTime());
        rootView.updateDateLabel(rootView.getToPickerEditText(), Calendar.getInstance().getTime());

        rootView.getFromToSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //String icao = AirportManager.getInstance().getAirportList().get(rootView.getDropdown().getSelectedItemPosition()).getIcao();
                rootView.getDropdown().setSelection(getDropdownIndexByIcao(isChecked ? mFormInfos.icaoTo : mFormInfos.icaoFrom));
            }
        });

        final DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDeparturePickerCalendar.set(Calendar.YEAR, year);
                mDeparturePickerCalendar.set(Calendar.MONTH, month);
                mDeparturePickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Calendar datePlus7 = HomeSearchFragment.this.getCalendarPlus7Days(mDeparturePickerCalendar);
                if (mArrivalPickerCalendar.getTime().getTime() < mDeparturePickerCalendar.getTime().getTime()) {
                    mArrivalPickerCalendar.setTime(mDeparturePickerCalendar.getTime());
                }
                else if (mArrivalPickerCalendar.getTime().getTime() > datePlus7.getTime().getTime()) {
                    mArrivalPickerCalendar.setTime(datePlus7.getTime());
                }
                rootView.updateDateLabel(rootView.getFromPickerEditText(), mDeparturePickerCalendar.getTime());
                rootView.updateDateLabel(rootView.getToPickerEditText(), mArrivalPickerCalendar.getTime());
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
                datePickerDialog.getDatePicker().setMinDate(mDeparturePickerCalendar.getTime().getTime());
                datePickerDialog.getDatePicker().setMaxDate(getCalendarPlus7Days(mDeparturePickerCalendar).getTime().getTime());
                datePickerDialog.show();
            }
        });

        final Spinner airportSelector = rootView.getDropdown();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, AirportManager.getInstance().getAirportNameList());
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        airportSelector.setAdapter(spinnerAdapter);
        airportSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isCheckedSwitch = rootView.getFromToSwitch().isChecked();
                String icao = AirportManager.getInstance().getAirportList().get(position).getIcao();
                if (isCheckedSwitch) {
                    mFormInfos.icaoTo = icao;
                }
                else {
                    mFormInfos.icaoFrom = icao;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button searchButton = rootView.getSearchButton();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo request etc
                /*Airport a = new Airport();
                a.setCode("12345");
                a.setCity("Bastia");
                a.setCountry("France");
                Airport b = new Airport();
                b.setCode("12345");
                b.setCity("Bastia");
                b.setCountry("France");
                List<Airport> airports = new ArrayList<>();
                airports.add(a);
                airports.add(b);
                //test
                SearchResultActivity.startActivity(getActivity(), airports);*/
                RequestManager.RequestInfos requestInfos = new RequestManager.RequestInfos("EDDF", 1517227200, 1517230800);
                RequestManager.getInstance().doGetRequestOnFlights(HomeSearchFragment.this, RequestManager.RequestType.DEPARTURE, requestInfos);
            }
        });

        return rootView;
    }

    private Calendar getCalendarPlus7Days(Calendar from) {
        Calendar datePlus7 = Calendar.getInstance();
        datePlus7.setTime(mDeparturePickerCalendar.getTime());
        datePlus7.add(Calendar.DAY_OF_MONTH, 7);
        return datePlus7;
    }

    private int getDropdownIndexByIcao(String icao) {
        List<Airport> airportsList = AirportManager.getInstance().getAirportList();
        for (int i=0; i < airportsList.size(); i++) {
            if (airportsList.get(i).getIcao().contentEquals(icao))
                return i;
        }
        return 0;
    }

    @Override
    public void onRequestSuccess(JsonArray jsonArray) {
        Log.v(TAG, "Request success!"+jsonArray);
        //todo startActivity
        SearchResultActivity.startActivity(getActivity(), Utils.convertFlightsJsonArrayToList(jsonArray));
    }

    @Override
    public void onRequestFail(String msg) {
        Log.e(TAG, msg);
    }

    public static class FormInfos {
        public String icaoFrom;
        public String icaoTo;

        public String departureDate;
        public String arrivalDate;
    }
}
