package com.example.shiv.movie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shiv.movie.R;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.fragments.MovieDetailFragment;

/**
 * The activity used to display information about the movie
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            Log.d(getClass().toString() , "Adding movie detail fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_movie_detail_container, new MovieDetailFragment())
                    .commit();
        }
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
}
