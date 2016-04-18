package com.example.shiv.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.movie.R;
import com.example.shiv.movie.commons.Constants;
import com.example.shiv.movie.holder.TrailerViewHolder;
import com.example.shiv.movie.objects.MovieObjectTrailer;

import java.util.ArrayList;

/**
 * Created by shiv on 14/4/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private Context context;
    private ArrayList<MovieObjectTrailer> movieObjectTrailerList;

    public TrailerAdapter(Context context , ArrayList<MovieObjectTrailer> movieObjectTrailerList){
        this.context = context;
        this.movieObjectTrailerList = movieObjectTrailerList;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video, null);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final MovieObjectTrailer movieObjectTrailer = movieObjectTrailerList.get(position);
        holder.getTrailerTextView().setText(Constants.TRAILER + Integer.toString(position + 1));
        holder.getPlayTrailerImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.YOUTUBE_VIDEO + movieObjectTrailer.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movieObjectTrailerList == null){
            return 0;
        }else{
            return movieObjectTrailerList.size();
        }
    }

    public void addAll(ArrayList<MovieObjectTrailer> movieObjectTrailerList ){
        this.movieObjectTrailerList.addAll(movieObjectTrailerList);
        notifyItemRangeInserted(0, movieObjectTrailerList.size());
    }
}
