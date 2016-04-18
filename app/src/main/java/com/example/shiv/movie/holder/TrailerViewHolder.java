package com.example.shiv.movie.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.movie.R;

/**
 * Created by shiv on 13/4/16.
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {

    private TextView trailerTextView;

    private ImageView playTrailerImageView;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        this.trailerTextView = (TextView)itemView.findViewById(R.id.list_item_video_name_text_view);
        this.playTrailerImageView = (ImageView)itemView.findViewById(R.id.list_item_video_image_view);
    }

    public TextView getTrailerTextView() {
        return trailerTextView;
    }

    public ImageView getPlayTrailerImageView() {
        return playTrailerImageView;
    }
}
