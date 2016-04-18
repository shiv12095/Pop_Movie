package com.example.shiv.movie.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.shiv.movie.BuildConfig;
import com.example.shiv.movie.R;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.commons.Constants;

/**
 * Created by shiv on 5/2/16.
 */
@Deprecated
public class FetchMovieDataService extends IntentService {

    private SharedPreferences sharedPreferences;

    public FetchMovieDataService(){
        super("FetchMovieDataService");
    }

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        fetchDiscoverMovieContent();
    }

    private void fetchDiscoverMovieContent(){
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(Constants.HTTP)
                .authority(Constants.MOVIEDB_API_URL_AUTHORITY)
                .appendPath("3")
                .appendPath(Constants.MOVIEDB_DISCOVER)
                .appendPath(Constants.MOVIE);
        if(sharedPreferences.getString(getResources().getString(R.string.pref_sort_by_key),
                getResources().getString(R.string.pref_sort_by_default)).
                equals(getResources().getString(R.string.pref_sort_by_default))){
            uriBuilder.
                    appendQueryParameter(Constants.MOVIEDB_SORT_BY_PARAMETER, Constants.MOVIEDB_POPULARITY_DESCENDING);
        }else{
            uriBuilder.
                    appendQueryParameter(Constants.MOVIEDB_SORT_BY_PARAMETER, Constants.MOVIEDB_RATING_DESCENDING);
        }
        uriBuilder.appendQueryParameter(Constants.MOVIEDB_API_KEY_PARAMETER, BuildConfig.API_KEY);

        Log.d(getClass().toString(), uriBuilder.build().toString());
        String string = Functions.getStringFromURL(uriBuilder.build().toString());
        Intent intent = new Intent(Constants.RECEIVED_MOVIE_DATA_EVENT);
        intent.putExtra(Constants.INTENT_EXTRA_STRING, string);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
