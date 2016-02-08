package com.example.shiv.movie.objects;



import com.example.shiv.movie.commons.Strings;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by shiv on 5/2/16.
 */
public class MovieObject {

    private long id;
    private String title;
    private String release_date;
    private String poster_path;
    private float vote_average;
    private String overview;
    private String poster_url;
    private DateTime dateTime;
    private boolean adult;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_url(){
        return Strings.MOVIEDB_IMAGE_URL_PATH + Strings.MOVIEDB_IMAGE_SIZE + this.poster_path;
    }

    public DateTime getDateTime() {
        this.dateTime = DateTime.parse(this.release_date, DateTimeFormat.forPattern(Strings.DATE_FORMAT));
        return dateTime;
    }

    public String getRating(){
        return this.vote_average + Strings.FORWARD_SLASH + Strings.TEN;
    }


}
