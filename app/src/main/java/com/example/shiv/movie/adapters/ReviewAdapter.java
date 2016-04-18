package com.example.shiv.movie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shiv.movie.R;
import com.example.shiv.movie.holder.GridViewHolder;
import com.example.shiv.movie.holder.ReviewViewHolder;
import com.example.shiv.movie.objects.MovieObject;
import com.example.shiv.movie.objects.MovieObjectReview;

import java.util.ArrayList;

/**
 * Created by shiv on 14/2/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

    private Context context;
    private ArrayList<MovieObjectReview> movieObjectReviewArrayList;


    public ReviewAdapter(Context context, ArrayList<MovieObjectReview> movieObjectReviewArrayList){
        this.context = context;
        this.movieObjectReviewArrayList = movieObjectReviewArrayList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_review, null);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        MovieObjectReview movieObjectReview = movieObjectReviewArrayList.get(position);
        holder.getAuthorTextView().setText(movieObjectReview.getAuthor());
        holder.getContentTextView().setText(movieObjectReview.getContent());

    }

    @Override
    public int getItemCount() {
        if(movieObjectReviewArrayList == null){
            return 0;
        }else{
            return movieObjectReviewArrayList.size();
        }
    }

    public void addAll(ArrayList<MovieObjectReview> movieObjectReviewArrayList ){
        this.movieObjectReviewArrayList.addAll(movieObjectReviewArrayList);
        notifyItemRangeInserted(0, movieObjectReviewArrayList.size());
    }
}
