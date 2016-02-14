package com.example.shiv.movie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.fragments.MovieGridFragment;
import com.example.shiv.movie.R;

/**
 * The main activity through which the app is launched.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            Log.d(getClass().toString(), "Main activity has saved instance");
            return;
        }
        MovieGridFragment movieGridFragment = new MovieGridFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.activity_main_linearlayout, movieGridFragment).commit();

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
}
