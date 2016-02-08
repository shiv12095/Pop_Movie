package com.example.shiv.movie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.shiv.movie.R;
import com.example.shiv.movie.objects.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 5/2/16.
 *
 * Custom adapter for the grid objects
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieObject> movieObjectArrayList;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, ArrayList<MovieObject> movieObjectArrayList){
        this.context = context;
        this.movieObjectArrayList = movieObjectArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return movieObjectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieObjectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder gridViewHolder;

        if(convertView==null){
            convertView = (View)layoutInflater.inflate(R.layout.list_item_grid, null);
            gridViewHolder = new GridViewHolder();
            gridViewHolder.imageView = (ImageView) convertView.findViewById(R.id.list_item_grid_imageview);
            gridViewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.list_item_grid_progressbar);
            gridViewHolder.progressBar.setVisibility(View.VISIBLE);
            convertView.setTag(gridViewHolder);
        }else{
            gridViewHolder = (GridViewHolder) convertView.getTag();
        }
        MovieObject movieObject = movieObjectArrayList.get(position);
        Picasso.with(context).load(movieObject.getPoster_url()).into(gridViewHolder.imageView);
        gridViewHolder.progressBar.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public void clear(){
        movieObjectArrayList.clear();
    }

    public void setDataList(ArrayList<MovieObject> movieObjectArrayList){
        this.movieObjectArrayList = movieObjectArrayList;
    }
}
