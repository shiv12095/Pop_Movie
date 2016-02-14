package com.example.shiv.movie.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shiv.movie.BuildConfig;
import com.example.shiv.movie.adapters.GridAdapter;
import com.example.shiv.movie.R;
import com.example.shiv.movie.activity.MovieDetailActivity;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.objects.MovieObjectResponse;
import com.example.shiv.movie.rest.client.MovieDBApiClient;
import com.example.shiv.movie.commons.Strings;
import com.example.shiv.movie.objects.MovieObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieGridFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridAdapter gridAdapter;
    private GridView gridView;
    private Gson gson = new Gson();
    private ArrayList<MovieObject> movieObjectArrayList;
    private MovieDBApiClient movieDBApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDBApiClient = new MovieDBApiClient();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        View view =  inflater.inflate(R.layout.fragment_movie_grid, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_movie_grid_swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        movieObjectArrayList =  new ArrayList<>();
        gridAdapter = new GridAdapter(getActivity(), movieObjectArrayList);
        gridView = (GridView)view.findViewById(R.id.fragment_movie_grid_gridview);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieObject movieObject = (MovieObject) gridAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(Strings.INTENT_EXTRA_STRING, gson.toJson(movieObject));
                startActivity(intent);
            }
        });
        return view;
    }

    private void getDataFromMovieDB(){
        swipeRefreshLayout.setRefreshing(true);
        if(Functions.isInternetEnabled(getActivity())) {
            Map<String, String> parameters = new HashMap<String, String>();
            if(sharedPreferences.getString(getResources().getString(R.string.pref_sort_by_key),
                    getResources().getString(R.string.pref_sort_by_default)).
                    equals(getResources().getString(R.string.pref_sort_by_default))){
                parameters.put(Strings.MOVIEDB_SORT_BY_PARAMETER, Strings.MOVIEDB_POPULARITY_DESCENDING);
            }else{
                parameters.put(Strings.MOVIEDB_SORT_BY_PARAMETER, Strings.MOVIEDB_RATING_DESCENDING);
            }
            parameters.put(Strings.MOVIEDB_API_KEY_PARAMETER, BuildConfig.API_KEY);

            movieDBApiClient.getService().getTrendingMovies(parameters, new Callback<MovieObjectResponse>() {
                @Override
                public void success(MovieObjectResponse movieObjectResponse, Response response) {
                    Log.d(getClass().toString(), "Success");
                    if(movieObjectResponse.getResults().isEmpty()){
                        Toast.makeText(getActivity(),
                                getActivity().getResources().getString(R.string.no_movie_data_found),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    movieObjectArrayList = new ArrayList<>();
                    swipeRefreshLayout.setRefreshing(false);
                    boolean allowNSFW = false;
                    if(!sharedPreferences.getString(getResources().getString(R.string.pref_nsfw_key),
                            getResources().getString(R.string.pref_nsfw_default)).
                            equals(getResources().getString(R.string.pref_nsfw_default))){
                        allowNSFW = true;
                    }
                    for(MovieObject movieObject : movieObjectResponse.getResults()){
                        if(!allowNSFW){
                            if(!movieObject.isAdult()){
                                movieObjectArrayList.add(movieObject);
                            }
                        }else{
                            movieObjectArrayList.add(movieObject);
                        }
                    }
                    gridAdapter.clear();
                    gridAdapter.setDataList(movieObjectArrayList);
                    gridAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }else{
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enable_internet),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getDataFromMovieDB();
    }
}
