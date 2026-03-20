package com.example.sportsapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.sportsapp.fragments.HomeFragment;

// main controller for fragments + toolbar nav
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar hookup (manual, keeps centered title working)
        toolbar = findViewById(R.id.toolbar);

        // toolbar back click
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // modern back handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        // keep arrow state synced with stack
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateBackArrow);

        // initial screen
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
        }

        updateBackArrow();
    }

    // central nav method used by fragments
    public void loadFragment(Fragment fragment, boolean addToBackStack) {

        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    // show arrow off home, hide on home
    private void updateBackArrow() {

        boolean show = getSupportFragmentManager().getBackStackEntryCount() > 0;

        if (show) {
            toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);

            // force white arrow
            if (toolbar.getNavigationIcon() != null) {
                toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
            }
        } else {
            toolbar.setNavigationIcon(null);
        }
    }
}