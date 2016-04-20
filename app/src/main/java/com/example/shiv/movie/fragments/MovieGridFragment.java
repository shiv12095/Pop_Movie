package com.example.shiv.movie.fragments;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shiv.movie.R;
import com.example.shiv.movie.adapters.GridAdapter;
import com.example.shiv.movie.commons.Constants;
import com.example.shiv.movie.commons.DBAdapter;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.objects.MovieObject;
import com.example.shiv.movie.objects.MovieObjectResponse;
import com.example.shiv.movie.rest.client.MovieDBApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Gson gson = new Gson();
    private ArrayList<MovieObject> movieObjectArrayList;
    private MovieDBApiClient movieDBApiClient;

    private GridLayoutManager gridLayoutManager;
    private GridAdapter gridAdapter;
    private RecyclerView recyclerView;

    private boolean writePermission;

    private DBAdapter dbAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDBApiClient = new MovieDBApiClient();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checkPermissionsGranted();
        if (writePermission) {
            dbAdapter = new DBAdapter();

        } else {
            Toast.makeText(getContext(), "The App requires the storage permission to work", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getDataFromMovieDB();
                                    }
                                }
        );
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_movie_grid_swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        movieObjectArrayList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_movie_grid_recycler_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridAdapter = new GridAdapter(getContext(), movieObjectArrayList, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(gridAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void getDataFromMovieDB() {
        swipeRefreshLayout.setRefreshing(true);
        if (Functions.isInternetEnabled(getActivity())) {
            if (sharedPreferences.getString(getResources().getString(R.string.pref_sort_by_key),
                    getResources().getString(R.string.pref_sort_by_default)).
                    equals(getResources().getString(R.string.pref_sort_by_popularity))) {
                getPopularMovies();
            } else if (sharedPreferences.getString(getResources().getString(R.string.pref_sort_by_key),
                    getResources().getString(R.string.pref_sort_by_rating)).
                    equals(getResources().getString(R.string.pref_sort_by_default))) {
                getTopRatedMovies();
            } else {
                getFavoriteMovies();
                return;
            }
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enable_internet),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getDataFromMovieDB();
    }

    private void getFavoriteMovies() {
        ArrayList<MovieObject> movieObjectArrayList = dbAdapter.getFavoriteMovies();
        swipeRefreshLayout.setRefreshing(false);
        gridAdapter.clear();
        ;
        gridAdapter.addAll(movieObjectArrayList);
    }

    private void getPopularMovies() {
        HashMap<String, String> parameters = Functions.getQueryParameterMap();
        parameters.put(Constants.MOVIEDB_SORT_BY_PARAMETER, Constants.MOVIEDB_POPULARITY_DESCENDING);
        movieDBApiClient.getService().getPopularMovies(parameters, new Callback<MovieObjectResponse>() {
            @Override
            public void success(MovieObjectResponse movieObjectResponse, Response response) {
                Log.d(getClass().toString(), "Success");
                if (movieObjectResponse.getResults().isEmpty()) {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.no_movie_data_found),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                movieObjectArrayList = new ArrayList<>();
                swipeRefreshLayout.setRefreshing(false);
                boolean allowNSFW = false;
                if (!sharedPreferences.getString(getResources().getString(R.string.pref_nsfw_key),
                        getResources().getString(R.string.pref_nsfw_default)).
                        equals(getResources().getString(R.string.pref_nsfw_default))) {
                    allowNSFW = true;
                }
                for (MovieObject movieObject : movieObjectResponse.getResults()) {
                    if (!allowNSFW) {
                        if (!movieObject.isAdult()) {
                            movieObjectArrayList.add(movieObject);
                            Log.d(getClass().toString(), Long.toString(movieObject.getId()));
                        }
                    } else {
                        movieObjectArrayList.add(movieObject);
                        Log.d(getClass().toString(), Long.toString(movieObject.getId()));
                    }
                }
                gridAdapter.clear();
                gridAdapter.addAll(movieObjectArrayList);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getTopRatedMovies() {
        HashMap<String, String> parameters = Functions.getQueryParameterMap();
        parameters.put(Constants.MOVIEDB_SORT_BY_PARAMETER, Constants.MOVIEDB_RATING_DESCENDING);
        movieDBApiClient.getService().getTopRatedMovies(parameters, new Callback<MovieObjectResponse>() {
            @Override
            public void success(MovieObjectResponse movieObjectResponse, Response response) {
                Log.d(getClass().toString(), "Success");
                if (movieObjectResponse.getResults().isEmpty()) {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.no_movie_data_found),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                movieObjectArrayList = new ArrayList<>();
                swipeRefreshLayout.setRefreshing(false);
                boolean allowNSFW = false;
                if (!sharedPreferences.getString(getResources().getString(R.string.pref_nsfw_key),
                        getResources().getString(R.string.pref_nsfw_default)).
                        equals(getResources().getString(R.string.pref_nsfw_default))) {
                    allowNSFW = true;
                }
                for (MovieObject movieObject : movieObjectResponse.getResults()) {
                    if (!allowNSFW) {
                        if (!movieObject.isAdult()) {
                            movieObjectArrayList.add(movieObject);
                            Log.d(getClass().toString(), Long.toString(movieObject.getId()));
                        }
                    } else {
                        movieObjectArrayList.add(movieObject);
                        Log.d(getClass().toString(), Long.toString(movieObject.getId()));
                    }
                }
                gridAdapter.clear();
                gridAdapter.addAll(movieObjectArrayList);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writePermission = true;
                } else {
                    writePermission = false;
                    Toast.makeText(getContext(), "The App requires the storage permission to work", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                return;
            }
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.MY_PERMISSIONS_REQUEST_STORAGE);
        }
    }

    private void checkPermissionsGranted() {
        /**
         * Check for write permission
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        } else {
            writePermission = true;
        }
    }
}
