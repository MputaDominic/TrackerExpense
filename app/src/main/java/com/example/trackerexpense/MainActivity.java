package com.example.trackerexpense;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.trackerexpense.ui.CategoryFragment;
import com.example.trackerexpense.ui.ReportsFragment;
import com.example.trackerexpense.ui.TransactionsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_transactions) {
                selectedFragment = new TransactionsFragment();
            } else if (itemId == R.id.navigation_reports) {
                selectedFragment = new ReportsFragment();
            } else if (itemId == R.id.navigation_category) {
                selectedFragment = new CategoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default selection
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.navigation_transactions);
        }
    }
}
