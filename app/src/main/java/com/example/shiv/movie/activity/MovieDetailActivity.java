package com.example.shiv.movie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.movie.R;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.commons.Strings;
import com.example.shiv.movie.objects.MovieObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * The activity used to display information about the movie
 */
public class MovieDetailActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle bundle = getIntent().getExtras();
        MovieObject movieObject = gson.fromJson(bundle.getString(Strings.INTENT_EXTRA_STRING), MovieObject.class);
        Picasso.with(this).load(movieObject.getPoster_url()).into((ImageView) findViewById(R.id.activity_movie_detail_poster_imageview));

        TextView titleTextView = (TextView)findViewById(R.id.activity_movie_detail_title_textview);
        titleTextView.setText(movieObject.getTitle());
        TextView overViewTextView = (TextView)findViewById(R.id.activity_movie_detail_overview_textview);
        overViewTextView.setText(movieObject.getOverview());
        TextView ratingTextView = (TextView)findViewById(R.id.activity_movie_detail_rating_textview);
        ratingTextView.setText(movieObject.getRating());
        TextView releaseYearTextView = (TextView)findViewById(R.id.activity_movie_detail_release_year_textview);
        releaseYearTextView.setText(String.valueOf(movieObject.getDateTime().getYear()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Functions.inflateApplicationMenu(getMenuInflater(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Functions.onItemSelectedApplicationMenu(item, this);
        return super.onOptionsItemSelected(item);
    }
}
