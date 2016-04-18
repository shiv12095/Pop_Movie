package com.example.shiv.movie.commons;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.shiv.movie.objects.MovieObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shiv on 18/4/16.
 */
public class DBAdapter {

    private SQLiteDatabase db;

    public SQLiteDatabase getDB() {
        return db;
    }

    public File getDatabaseDirectory() {
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + File.separator + Constants.APP_NAME);
        if (!directory.exists()) {
            Log.d(getClass().toString(), "Created directory " + directory.getAbsolutePath());
            directory.mkdir();
        }
        return directory;
    }

    public DBAdapter() {
        File dataBaseDirectory = getDatabaseDirectory();
        db = SQLiteDatabase.openDatabase(dataBaseDirectory.getAbsolutePath() + File.separator + Constants.APP_DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("CREATE TABLE IF NOT EXISTS Movie(movie_ID INT UNIQUE,title VARCHAR,release_date VARCHAR,poster_path VARCHAR, vote_average FLOAT, overview VARCHAR);");
    }


    public void insertMovie(MovieObject movieObject) {
        db.execSQL("INSERT INTO Movie VALUES(" + movieObject.getId() +  "," + "'" + movieObject.getTitle() + "'" + "," + "'" + movieObject.getRelease_date() + "'" + "," + "'" + movieObject.getPoster_path() + "'" + "," +  movieObject.getVote_average() + "," + "'" + movieObject.getOverview() + "'" +")");
    }

    public ArrayList<MovieObject> getFavoriteMovies() {
        ArrayList<MovieObject> list = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM Movie;", null);
        while (result.moveToNext()) {
            MovieObject movieObject = new MovieObject();
            movieObject.setId(result.getInt(result.getColumnIndex("movie_ID")));
            movieObject.setTitle(result.getString(result.getColumnIndex("title")));
            movieObject.setRelease_date(result.getString(result.getColumnIndex("release_date")));
            movieObject.setPoster_path(result.getString(result.getColumnIndex("poster_path")));
            movieObject.setVote_average(result.getFloat(result.getColumnIndex("vote_average")));
            movieObject.setOverview(result.getString(result.getColumnIndex("overview")));
            list.add(movieObject);
        }
        return list;
    }

    public boolean isFavoriteMovie(long id){
        Cursor result = db.rawQuery("SELECT * FROM Movie WHERE movie_id = " + id + ";", null);
        int count = 0;
        while (result.moveToNext()){
            return true;
        }
        return false;
    }

    public boolean deleteMovie(long id){
        return db.delete("Movie","movie_id= " + id,null)>0;
    }

}
