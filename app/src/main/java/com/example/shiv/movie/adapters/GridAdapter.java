package com.example.shiv.movie.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.movie.R;
import com.example.shiv.movie.activity.MovieDetailActivity;
import com.example.shiv.movie.commons.Constants;
import com.example.shiv.movie.fragments.MovieDetailFragment;
import com.example.shiv.movie.holder.GridViewHolder;
import com.example.shiv.movie.objects.MovieObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 5/2/16.
 *
 * Custom adapter for the grid objects
 */
public class GridAdapter extends RecyclerView.Adapter<GridViewHolder> {

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<MovieObject> movieObjectArrayList;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    public GridAdapter(Context context, ArrayList<MovieObject> movieObjectArrayList, FragmentManager fragmentManager){
        this.context = context;
        this.movieObjectArrayList = movieObjectArrayList;
        this.fragmentManager = fragmentManager;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_grid, null);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        final MovieObject movieObject = movieObjectArrayList.get(position);
        Picasso.with(context).load(movieObject.getPoster_url()).into(holder.getImageView());
        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getBoolean(Constants.IS_TWO_PANE, false)) {
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    Bundle args = new Bundle();
                    args.putString(Constants.INTENT_EXTRA_STRING, gson.toJson(movieObject));
                    movieDetailFragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.activity_main_movie_detail_container, movieDetailFragment).commit();
                }else {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_STRING, gson.toJson(movieObject));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movieObjectArrayList == null){
            return 0;
        }else{
            return movieObjectArrayList.size();
        }
    }

    public void clear(){
        int listSize = movieObjectArrayList.size();
        this.movieObjectArrayList = new ArrayList<>();
        notifyItemRangeRemoved(0, listSize);
    }

    public void addAll(ArrayList<MovieObject> movieObjectArrayList){
        this.movieObjectArrayList.addAll(movieObjectArrayList);
        notifyItemRangeInserted(0, movieObjectArrayList.size());
    }
}
