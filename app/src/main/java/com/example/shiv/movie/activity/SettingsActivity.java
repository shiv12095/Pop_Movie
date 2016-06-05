package com.example.shiv.movie.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.shiv.movie.R;
import com.example.shiv.movie.fragments.SettingsFragment;

/**
 * Created by shiv on 3/2/16.
 */


/**
 * The activity used to modify the settings of the application
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(savedInstanceState != null){
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().
                add(R.id.activity_settings_linearlayout, settingsFragment).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}