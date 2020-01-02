package com.christophenasica.flyscanner.core.fragments;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.AirportManager;
import com.christophenasica.flyscanner.core.MainViewModel;
import com.christophenasica.flyscanner.core.RequestManager;
import com.christophenasica.flyscanner.core.Utils;
import com.christophenasica.flyscanner.core.activities.SearchResultActivity;
import com.christophenasica.flyscanner.core.views.FlightFormView;
import com.christophenasica.flyscanner.data.Airport;
import com.christophenasica.flyscanner.data.Flight;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeSearchFragment extends Fragment {

    private static final String TAG = HomeSearchFragment.class.getSimpleName();

    private final SavedInfos mSavedInfos = new SavedInfos();

    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FlightFormView rootView = new FlightFormView(getContext());

        if (mSavedInfos.icaoFrom == null && mSavedInfos.icaoTo == null) {
            String defaultIcao = AirportManager.getInstance().getAirportList().get(0).getIcao();
            mSavedInfos.icaoFrom = defaultIcao;
            mSavedInfos.icaoTo = defaultIcao;
        }

        mMainViewModel.getDepartureCalendar().postValue(getCalendarPlusXDays(Calendar.getInstance(), -7));
        mMainViewModel.getArrivalCalendar().postValue(getCalendarPlusXDays(Calendar.getInstance(), -1));

        mMainViewModel.getDepartureCalendar().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar dCalendar) {
                if (dCalendar != null) {
                    updateDateLabel(rootView.getFromPickerEditText(), dCalendar.getTime());
                }
            }
        });

        mMainViewModel.getArrivalCalendar().observe(this, new Observer<Calendar>() {
            @Override
            public void onChanged(@Nullable Calendar aCalendar) {
                if (aCalendar != null)
                    updateDateLabel(rootView.getToPickerEditText(), aCalendar.getTime());
            }
        });

        mMainViewModel.getCurrentFlights().observe(this, new Observer<List<Flight>>() {
            @Override
            public void onChanged(@Nullable List<Flight> flights) {
                if (flights != null) {
                    mMainViewModel.getIsLoading().postValue(false);
                    SearchResultActivity.startActivity(getActivity(), flights);
                }
            }
        });

        mMainViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null)
                    rootView.getLoadingSpinner().setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        rootView.getFromToSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rootView.getDropdown().setSelection(getDropdownIndexByIcao(isChecked ? mSavedInfos.icaoTo : mSavedInfos.icaoFrom));
            }
        });

        final DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dCalendar = Calendar.getInstance();
                dCalendar.set(Calendar.YEAR, year);
                dCalendar.set(Calendar.MONTH, month);
                dCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mMainViewModel.getDepartureCalendar().postValue(dCalendar);
            }
        };

        final DatePickerDialog.OnDateSetListener arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.YEAR, year);
                aCalendar.set(Calendar.MONTH, month);
                aCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mMainViewModel.getArrivalCalendar().postValue(aCalendar);
            }
        };

        rootView.getFromPickerEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar dCalendar = mMainViewModel.getDepartureCalendar().getValue();
                if (dCalendar != null) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), departureDateSetListener, dCalendar.get(Calendar.YEAR), dCalendar.get(Calendar.MONTH), dCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
                    datePickerDialog.show();
                }
            }
        });

        rootView.getToPickerEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar aCalendar = mMainViewModel.getArrivalCalendar().getValue();
                Calendar dCalendar = mMainViewModel.getDepartureCalendar().getValue();
                if (aCalendar != null && dCalendar != null) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), arrivalDateSetListener, aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(getCalendarPlusXDays(dCalendar, -1).getTime().getTime());
                    datePickerDialog.show();
                }
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
                    mSavedInfos.icaoTo = icao;
                }
                else {
                    mSavedInfos.icaoFrom = icao;
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
                Calendar dCalendar = mMainViewModel.getDepartureCalendar().getValue();
                Calendar aCalendar = mMainViewModel.getArrivalCalendar().getValue();

                String icao = AirportManager.getInstance().getAirportList().get(rootView.getDropdown().getSelectedItemPosition()).getIcao();
                RequestManager.RequestType requestType = rootView.getFromToSwitch().isChecked() ? RequestManager.RequestType.ARRIVAL : RequestManager.RequestType.DEPARTURE;

                if (dCalendar != null && aCalendar != null && Utils.isStringValid(icao)) {
                    if (HomeSearchFragment.this.getCalendarPlusXDays(dCalendar, 7).getTimeInMillis() > aCalendar.getTimeInMillis()) {
                        int begin = (int) (dCalendar.getTimeInMillis() / 1000);
                        int end = (int) (aCalendar.getTimeInMillis() / 1000);

                        RequestManager.RequestInfos requestInfos = new RequestManager.RequestInfos(icao, begin, end);
                        RequestManager.getInstance().doGetRequestOnFlights(requestType, requestInfos);
                        mMainViewModel.getIsLoading().postValue(true);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.alert_interval_too_big, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.e(TAG, "Error while trying to send form: dates or icao incorrect!");
                }
            }
        });

        return rootView;
    }

    private Calendar getCalendarPlusXDays(Calendar from, int nbDays) {
        Calendar datePlusX = Calendar.getInstance();
        datePlusX.setTime(from.getTime());
        datePlusX.add(Calendar.DAY_OF_MONTH, nbDays);
        return datePlusX;
    }

    private int getDropdownIndexByIcao(String icao) {
        List<Airport> airportsList = AirportManager.getInstance().getAirportList();
        for (int i=0; i < airportsList.size(); i++) {
            if (airportsList.get(i).getIcao().contentEquals(icao))
                return i;
        }
        return 0;
    }

    private void updateDateLabel(EditText dateEditText, Date date) {
        dateEditText.setText(Utils.dateToString(date));
    }

    public static class SavedInfos {
        public String icaoFrom;
        public String icaoTo;
    }
}
