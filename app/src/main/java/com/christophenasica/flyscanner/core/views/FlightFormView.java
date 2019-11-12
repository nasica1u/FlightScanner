package com.christophenasica.flyscanner.core.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.christophenasica.flyscanner.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FlightFormView extends RelativeLayout {
    private static final int ID = R.layout.flight_form_layout;

    private TextView mDropdownLabel;
    private Spinner mDropdown;

    private TextView mFromLabel;
    private Switch mFromToSwitch;
    private TextView mToLabel;

    private TextView mFromPickerLabel;
    private EditText mFromPickerEditText;
    private TextView mToPickerLabel;
    private EditText mToPickerEditText;

    private final Calendar mLeftPickerCalendar = Calendar.getInstance();
    private final Calendar mRightPickerCalendar = Calendar.getInstance();

    public FlightFormView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        initView();
    }

    public FlightFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        initView();
    }

    public FlightFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(ID, this, true);
        initView();
    }

    private void bindViews()
    {
        mDropdownLabel = findViewById(R.id.dropDownLabel);
        mDropdown = findViewById(R.id.airportDropdown);
        mFromLabel = findViewById(R.id.fromLabel);
        mFromToSwitch = findViewById(R.id.fromToSwitch);
        mToLabel = findViewById(R.id.toLabel);
        mFromPickerLabel = findViewById(R.id.fromPickerLabel);
        mFromPickerEditText = findViewById(R.id.fromPicker);
        mToPickerLabel = findViewById(R.id.toPickerLabel);
        mToPickerEditText = findViewById(R.id.toPicker);
    }

    private void initView()
    {
        bindViews();

        final DatePickerDialog.OnDateSetListener departureDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mLeftPickerCalendar.set(Calendar.YEAR, year);
                mLeftPickerCalendar.set(Calendar.MONTH, month);
                mLeftPickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(mFromPickerEditText, mLeftPickerCalendar.getTime());
            }
        };

        final DatePickerDialog.OnDateSetListener arrivalDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mRightPickerCalendar.set(Calendar.YEAR, year);
                mRightPickerCalendar.set(Calendar.MONTH, month);
                mRightPickerCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(mToPickerEditText, mRightPickerCalendar.getTime());
            }
        };

        mFromPickerEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), departureDateSetListener, mLeftPickerCalendar.get(Calendar.YEAR), mLeftPickerCalendar.get(Calendar.MONTH), mLeftPickerCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mToPickerEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), arrivalDateSetListener, mRightPickerCalendar.get(Calendar.YEAR), mRightPickerCalendar.get(Calendar.MONTH), mRightPickerCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public Calendar getDeparture() {
        return mLeftPickerCalendar;
    }

    public Calendar getArrival() {
        return mRightPickerCalendar;
    }

    private void updateDateLabel(EditText dateEditText, Date date) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(date));
    }
}
