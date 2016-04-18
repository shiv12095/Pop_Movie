package com.example.shiv.movie.rest.service;

import com.example.shiv.movie.objects.MovieObjectResponse;
import com.example.shiv.movie.objects.MovieObjectReviewResponse;
import com.example.shiv.movie.objects.MovieObjectTrailerResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by shiv on 14/2/16.
 */
public interface MovieDBApi {


    @GET("/movie/top_rated")
    public void getTopRatedMovies(@QueryMap Map<String, String> options,  Callback<MovieObjectResponse> callback);

    @GET("/movie/popular")
    public void getPopularMovies(@QueryMap Map<String, String> options,  Callback<MovieObjectResponse> callback);

    @GET("/movie/{id}/reviews")
    public void getMovieReview(@Path("id") Long id, @QueryMap Map<String, String> options, Callback<MovieObjectReviewResponse> callback);

    @GET("/movie/{id}/videos")
    public void getMovieVideos(@Path("id") Long id, @QueryMap Map<String, String> options, Callback<MovieObjectTrailerResponse> callback);
}
