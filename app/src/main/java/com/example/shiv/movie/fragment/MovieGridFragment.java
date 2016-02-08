package com.example.shiv.movie.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shiv.movie.adapters.GridAdapter;
import com.example.shiv.movie.R;
import com.example.shiv.movie.activity.MovieDetailActivity;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.service.FetchMovieDataService;
import com.example.shiv.movie.commons.Strings;
import com.example.shiv.movie.objects.MovieObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class MovieGridFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridAdapter gridAdapter;
    private GridView gridView;
    private Gson gson = new Gson();
    private ArrayList<MovieObject> movieObjectArrayList;

    private BroadcastReceiver fetchDataBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            movieObjectArrayList = new ArrayList<>();
            String json = intent.getStringExtra(Strings.INTENT_EXTRA_STRING);
            if(json == null){
                Toast.makeText(getActivity(),
                        getActivity().getResources().getString(R.string.no_movie_data_found),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(getClass().toString(), json);
            swipeRefreshLayout.setRefreshing(false);
            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonObject()
                    .getAsJsonArray(Strings.RESULTS);

            boolean allowNSFW = false;
            if(!sharedPreferences.getString(getResources().getString(R.string.pref_nsfw_key),
                    getResources().getString(R.string.pref_nsfw_default)).
                    equals(getResources().getString(R.string.pref_nsfw_default))){
                allowNSFW = true;
            }
            for(JsonElement jsonElement : jsonArray){
                MovieObject movieObject = gson.fromJson(jsonElement.toString(), MovieObject.class);
                Log.d(getClass().toString(), String.valueOf(movieObject.isAdult()));
                if(!allowNSFW){
                    if(!movieObject.isAdult()){
                        movieObjectArrayList.add(movieObject);
                    }
                }
            }
            gridAdapter.clear();
            gridAdapter.setDataList(movieObjectArrayList);
            gridAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(fetchDataBroadCastReceiver,
                new IntentFilter((Strings.RECEIVED_MOVIE_DATA_EVENT)));
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(fetchDataBroadCastReceiver,
                new IntentFilter((Strings.RECEIVED_MOVIE_DATA_EVENT)));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(fetchDataBroadCastReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().toString(), "On Destroy called");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(fetchDataBroadCastReceiver);
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
            Intent serviceIntent = new Intent(getActivity(), FetchMovieDataService.class);
            getActivity().startService(serviceIntent);
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
