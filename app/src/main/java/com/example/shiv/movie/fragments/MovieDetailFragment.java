package com.example.shiv.movie.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.movie.R;
import com.example.shiv.movie.adapters.ReviewAdapter;
import com.example.shiv.movie.adapters.TrailerAdapter;
import com.example.shiv.movie.commons.DBAdapter;
import com.example.shiv.movie.commons.Functions;
import com.example.shiv.movie.commons.Constants;
import com.example.shiv.movie.objects.MovieObject;
import com.example.shiv.movie.objects.MovieObjectReview;
import com.example.shiv.movie.objects.MovieObjectReviewResponse;
import com.example.shiv.movie.objects.MovieObjectTrailer;
import com.example.shiv.movie.objects.MovieObjectTrailerResponse;
import com.example.shiv.movie.rest.client.MovieDBApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private Gson gson = new Gson();
    private MovieDBApiClient movieDBApiClient;
    private TextView reviewTextView;
    private TextView trailerTextView;
    private MovieObject movieObject;
    private Button favoriteButton;

    private ArrayList<MovieObjectReview> reviewList = new ArrayList<>();
    private RecyclerView reviewReviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ImageView reviewExpandButton;

    private ArrayList<MovieObjectTrailer> trailerList = new ArrayList<>();
    private RecyclerView trailerReviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ImageView trailerExpandButton;

    private SharedPreferences sharedPreferences;
    private DBAdapter dbAdapter;

    private boolean isFavorite;

    private String REVIEW_LIST = "review_list";
    private String TRAILER_LIST = "trailer_list";

    private Context context;

    public MovieDetailFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        fetchTrailers();
        fetchReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TRAILER_LIST, gson.toJson(trailerList));
        outState.putString(REVIEW_LIST, gson.toJson(reviewList));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieDBApiClient = new MovieDBApiClient();
        dbAdapter = new DBAdapter();

        Bundle bundle;

        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constants.IS_TWO_PANE, false)) {
            bundle = getArguments();
        } else {
            bundle = getActivity().getIntent().getExtras();
        }

        if (bundle == null) {
            return;
        }

        movieObject = gson.fromJson(bundle.getString(Constants.INTENT_EXTRA_STRING), MovieObject.class);

        reviewTextView = (TextView) view.findViewById(R.id.activity_movie_detail_review_textview);
        reviewExpandButton = (ImageView) view.findViewById(R.id.activity_movie_detail_review_image_view);

        trailerTextView = (TextView) view.findViewById(R.id.activity_movie_detail_trailer_textview);
        trailerExpandButton = (ImageView) view.findViewById(R.id.activity_movie_detail_trailer_image_view);

        reviewReviewRecyclerView = (RecyclerView) view.findViewById(R.id.activity_movie_detail_review_recycler_view);
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getContext());
        reviewLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        reviewReviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
        reviewReviewRecyclerView.setAdapter(reviewAdapter);

        trailerReviewRecyclerView = (RecyclerView) view.findViewById(R.id.activity_movie_detail_trailer_recycler_view);
        LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(getContext());
        trailerLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trailerAdapter = new TrailerAdapter(getContext(), trailerList);
        trailerReviewRecyclerView.setLayoutManager(trailerLinearLayoutManager);
        trailerReviewRecyclerView.setAdapter(trailerAdapter);


        Picasso.with(getContext()).load(movieObject.getPoster_url()).into((ImageView) view.findViewById(R.id.activity_movie_detail_poster_imageview));

        TextView titleTextView = (TextView) view.findViewById(R.id.activity_movie_detail_title_textview);
        titleTextView.setText(movieObject.getTitle());
        TextView overViewTextView = (TextView) view.findViewById(R.id.activity_movie_detail_overview_textview);
        overViewTextView.setText(movieObject.getOverview());
        TextView ratingTextView = (TextView) view.findViewById(R.id.activity_movie_detail_rating_textview);
        ratingTextView.setText(movieObject.getRating());
        TextView releaseYearTextView = (TextView) view.findViewById(R.id.activity_movie_detail_release_year_textview);
        releaseYearTextView.setText(String.valueOf(movieObject.getDateTime().getYear()));

        isFavorite = dbAdapter.isFavoriteMovie(movieObject.getId());

        favoriteButton = (Button) view.findViewById(R.id.activity_movie_detail_favorite_button);

        setFavoriteButtonFunctionality();

        if (savedInstanceState != null) {
            TypeToken<ArrayList<MovieObjectReview>> reviewTypeToken = new TypeToken<ArrayList<MovieObjectReview>>() {
            };
            TypeToken<ArrayList<MovieObjectTrailer>> trailerTypeToken = new TypeToken<ArrayList<MovieObjectTrailer>>() {
            };
            reviewList = gson.fromJson(savedInstanceState.getString(REVIEW_LIST), reviewTypeToken.getType());
            trailerList = gson.fromJson(savedInstanceState.getString(TRAILER_LIST), trailerTypeToken.getType());
            reviewAdapter.addAll(reviewList);
            trailerAdapter.addAll(trailerList);
            toggleReviewView();
            toggleTrailerView();
            return;
        }
    }

    private void setFavoriteButtonFunctionality() {
        if (isFavorite) {
            favoriteButton.setText(context.getResources().getString(R.string.unfavorite));
        } else {
            favoriteButton.setText(context.getResources().getString(R.string.favorite));
        }
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    dbAdapter.deleteMovie(movieObject.getId());
                    favoriteButton.setText(context.getResources().getString(R.string.favorite));
                    isFavorite = false;
                } else {
                    dbAdapter.insertMovie(movieObject);
                    favoriteButton.setText(context.getResources().getString(R.string.unfavorite));
                    isFavorite = true;
                }
            }
        });
    }

    private void fetchReviews() {
        HashMap<String, String> parameters = Functions.getQueryParameterMap();
        movieDBApiClient.getService().getMovieReview(movieObject.getId(), parameters, new Callback<MovieObjectReviewResponse>() {
            @Override
            public void success(MovieObjectReviewResponse movieObjectReviewResponse, Response response) {
                Log.d(getClass().toString(), "Success here");
                if (movieObjectReviewResponse.getResults().isEmpty()) {
                    reviewTextView.setText(context.getResources().getString(R.string.no_reviews));
                    reviewExpandButton.setVisibility(View.INVISIBLE);
                } else {
                    reviewTextView.setText(context.getResources().getString(R.string.reviews));
                    reviewExpandButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_less));
                    reviewList = movieObjectReviewResponse.getResults();
                    reviewAdapter.addAll(reviewList);
                }
                toggleReviewView();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to fetch reviews");
                reviewTextView.setText(context.getResources().getString(R.string.no_reviews));
            }
        });
    }

    private void fetchTrailers() {
        HashMap<String, String> parameters = Functions.getQueryParameterMap();
        movieDBApiClient.getService().getMovieVideos(movieObject.getId(), parameters, new Callback<MovieObjectTrailerResponse>() {
            @Override
            public void success(MovieObjectTrailerResponse movieObjectTrailerResponse, Response response) {
                Log.d(getClass().toString(), "Success here");
                if (movieObjectTrailerResponse.getResults().isEmpty()) {
                    trailerTextView.setText(context.getResources().getString(R.string.no_trailers));
                    trailerExpandButton.setVisibility(View.INVISIBLE);
                } else {
                    trailerTextView.setText(context.getResources().getString(R.string.trailers));
                    trailerList = movieObjectTrailerResponse.getResults();
                    trailerAdapter.addAll(trailerList);
                }
                toggleTrailerView();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to fetch trailers");
                reviewTextView.setText(context.getResources().getString(R.string.no_trailers));
                trailerExpandButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void toggleReviewView() {
        if (reviewList.isEmpty()) {
            reviewTextView.setText(context.getResources().getString(R.string.no_reviews));
            reviewExpandButton.setVisibility(View.INVISIBLE);
        } else {
            reviewTextView.setText(context.getResources().getString(R.string.reviews));
            reviewExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewReviewRecyclerView.getVisibility() == View.VISIBLE) {
                        reviewReviewRecyclerView.setVisibility(View.GONE);
                        reviewExpandButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_more));
                    } else {
                        reviewReviewRecyclerView.setVisibility(View.VISIBLE);
                        reviewExpandButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_less));
                    }
                }
            });
        }
    }

    private void toggleTrailerView() {
        if (trailerList.isEmpty()) {
            trailerTextView.setText(getResources().getString(R.string.no_trailers));
            trailerExpandButton.setVisibility(View.INVISIBLE);
        } else {
            trailerTextView.setText(getResources().getString(R.string.trailers));
            trailerExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trailerReviewRecyclerView.getVisibility() == View.VISIBLE) {
                        trailerReviewRecyclerView.setVisibility(View.GONE);
                        trailerExpandButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more));
                    } else {
                        trailerReviewRecyclerView.setVisibility(View.VISIBLE);
                        trailerExpandButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less));
                    }
                }
            });
        }
    }
}
