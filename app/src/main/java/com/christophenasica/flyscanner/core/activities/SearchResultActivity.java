package com.christophenasica.flyscanner.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.fragments.SearchResultFragment;
import com.christophenasica.flyscanner.data.Flight;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    public static void startActivity(Activity from, List<Flight> flights) {
        Intent intent = new Intent(from, SearchResultActivity.class);
        if (flights != null) {
            intent.putParcelableArrayListExtra(SearchResultFragment.FLIGHTS_PARAM, (ArrayList<Flight>) flights);
        }
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity_layout);

        ArrayList<Flight> flightArrayList = getIntent().getParcelableArrayListExtra(SearchResultFragment.FLIGHTS_PARAM);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.searchFragmentContainer, SearchResultFragment.newResultFragment(flightArrayList)).commit();
    }
}
