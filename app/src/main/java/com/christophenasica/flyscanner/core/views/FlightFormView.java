package com.christophenasica.flyscanner.core.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

    private Button mSearchButton;

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
        mSearchButton = findViewById(R.id.searchButton);
    }

    private void initView()
    {
        bindViews();

        //other init here
    }

    public void updateDateLabel(EditText dateEditText, Date date) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(date));
    }

    public TextView getDropdownLabel() {
        return mDropdownLabel;
    }

    public Spinner getDropdown() {
        return mDropdown;
    }

    public TextView getFromLabel() {
        return mFromLabel;
    }

    public Switch getFromToSwitch() {
        return mFromToSwitch;
    }

    public TextView getToLabel() {
        return mToLabel;
    }

    public TextView getFromPickerLabel() {
        return mFromPickerLabel;
    }

    public EditText getFromPickerEditText() {
        return mFromPickerEditText;
    }

    public TextView getToPickerLabel() {
        return mToPickerLabel;
    }

    public EditText getToPickerEditText() {
        return mToPickerEditText;
    }

    public Button getSearchButton() {
        return mSearchButton;
    }
}
