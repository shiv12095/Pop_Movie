package com.example.shiv.movie.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shiv.movie.R;
import com.example.shiv.movie.commons.Constants;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.fragments.MovieDetailFragment;

/**
 * The main activity through which the app is launched.
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.activity_main_movie_detail_container) != null) {
            mTwoPane = true;
            editor.putBoolean(Constants.IS_TWO_PANE, true);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_movie_detail_container, new MovieDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
            editor.putBoolean(Constants.IS_TWO_PANE, false);
        }
        editor.apply();
        if (savedInstanceState != null) {
            Log.d(getClass().toString(), "Main activity has saved instance");
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Functions.inflateApplicationMenu(getMenuInflater(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Functions.onItemSelectedApplicationMenu(item, this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
