package com.christophenasica.flyscanner.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.christophenasica.flyscanner.R;
import com.christophenasica.flyscanner.core.fragments.SearchResultFragment;

public class SearchResultActivity extends BaseNavbarActivity {

    private static final int ID = R.layout.search_result_activity_layout;

    public static void startActivity(Activity from) {
        Intent intent = new Intent(from, SearchResultActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init NavBar button
        getNavBarLeftButton().setImageResource(R.drawable.ic_arrow_back_white);
        getNavBarLeftButton().setVisibility(View.VISIBLE);
        getNavBarLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("frito" , "avant : "+getSupportFragmentManager().getBackStackEntryCount());
                if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                    onBackPressed();
                else
                    getSupportFragmentManager().popBackStack();
                //getSupportFragmentManager().popBackStack();
                Log.e("frito" , "après : "+getSupportFragmentManager().getBackStackEntryCount());
            }
        });

        initBaseFragment();
    }

    private void initBaseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.searchFragmentContainer, new SearchResultFragment()).addToBackStack(null).commit();
    }

    @Override
    protected int getContentLayoutId() {
        return ID;
    }
}
