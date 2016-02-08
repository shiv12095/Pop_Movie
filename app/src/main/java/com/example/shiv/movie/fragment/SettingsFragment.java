package com.example.shiv.movie.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.shiv.movie.R;

/**
 * Created by shiv on 6/2/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);
    }
}
