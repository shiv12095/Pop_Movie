package com.example.shiv.movie.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shiv.movie.R;


/**
 * Created by shiv on 5/2/16.
 *
 * Custom view to manage the grid view
 */
public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ImageView imageView;

    public GridViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView)itemView.findViewById(R.id.list_item_grid_imageview);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked here" , Toast.LENGTH_SHORT).show();
    }
}
