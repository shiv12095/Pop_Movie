package com.example.shiv.movie.rest.client;

import com.example.shiv.movie.commons.Strings;
import com.example.shiv.movie.rest.service.MovieDBApi;

import retrofit.RestAdapter;

/**
 * Created by shiv on 14/2/16.
 */
public class MovieDBApiClient {

    private String BASE_URL = Strings.MOVIEDB_REST_API_PATH;
    private MovieDBApi movieDBApi;

    public MovieDBApiClient(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        movieDBApi = restAdapter.create(MovieDBApi.class);
    }

    public MovieDBApi getService(){
        return this.movieDBApi;
    }
}
