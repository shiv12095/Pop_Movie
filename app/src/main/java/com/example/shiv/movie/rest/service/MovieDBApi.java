package com.example.shiv.movie.rest.service;

import com.example.shiv.movie.objects.MovieObjectResponse;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by shiv on 14/2/16.
 */
public interface MovieDBApi {


    @GET("/discover/movie")
    public void getTrendingMovies(@QueryMap Map<String, String> options,  Callback<MovieObjectResponse> callback);

    @GET("/movie/{id}/videos")
    public void getMovieVideos(@Path("id") Long id);

    @GET("/movie/{id}/reviews")
    public void getMovieReviews(@Path("id") Long id);
}
