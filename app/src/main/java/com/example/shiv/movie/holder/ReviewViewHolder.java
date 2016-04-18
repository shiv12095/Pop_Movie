package com.example.shiv.movie.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.shiv.movie.R;

/**
 * Created by shiv on 14/2/16.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder{

    private TextView authorTextView;
    private TextView contentTextView;

    public ReviewViewHolder(View view) {
        super(view);
        this.authorTextView = (TextView)view.findViewById(R.id.list_item_review__content_textview);
        this.contentTextView = (TextView)view.findViewById(R.id.list_item_review_author_textview);
    }

    public TextView getAuthorTextView() {
        return authorTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }
}
