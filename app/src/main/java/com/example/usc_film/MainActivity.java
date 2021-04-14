package com.example.usc_film;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit(); // Start Home when app first launches
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home_btn:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.search_btn:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.watchlist_btn:
                    selectedFragment = new WatchlistFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, selectedFragment).commit();

            return true;
        }
    };
}
