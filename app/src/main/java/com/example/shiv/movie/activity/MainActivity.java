package com.example.shiv.movie.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
        requestPermissions();
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
        editor.commit();
        if (savedInstanceState != null) {
            Log.d(getClass().toString(), "Main activity has saved instance");
            return;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "The App requires the storage permission to work", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.MY_PERMISSIONS_REQUEST_STORAGE);
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
}
