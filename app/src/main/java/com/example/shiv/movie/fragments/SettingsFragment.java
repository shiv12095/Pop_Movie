package com.example.shiv.movie.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.shiv.movie.R;

/**
 * Created by shiv on 6/2/16.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private String originalValueSortBy;

    private String originalValueNSFW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);
        originalValueSortBy = getPreferenceScreen().getSharedPreferences().getString(getResources().getString(R.string.pref_sort_by_key), getResources().getString(R.string.pref_sort_by_popularity));
        originalValueNSFW = getPreferenceScreen().getSharedPreferences().getString(getResources().getString(R.string.pref_nsfw_key), getResources().getString(R.string.pref_nsfw_default));
        Log.d(getClass().toString() , "Original Value sort by : " + originalValueSortBy);
        Log.d(getClass().toString() , "Original Value NSFW: " + originalValueNSFW);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(getClass().toString(), "On shared preference changed for key  : " + key);
        Log.d(getClass().toString(), "The new value is " + getPreferenceScreen().getSharedPreferences().getString(key, ""));
        if(key.equals(getResources().getString(R.string.pref_sort_by_key)) && !originalValueSortBy.equals(getPreferenceScreen().getSharedPreferences().getString(key, ""))){
            MovieGridFragment.setRecreateView(true);
        }
        if(key.equals(getResources().getString(R.string.pref_nsfw_key)) && !originalValueNSFW.equals(getPreferenceScreen().getSharedPreferences().getString(key, ""))){
            MovieGridFragment.setRecreateView(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
