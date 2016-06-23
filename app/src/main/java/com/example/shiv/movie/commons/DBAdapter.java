package com.example.shiv.movie.commons;

import android.content.ContentValues;
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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME + "(" + Constants.COLUMN_HEADER_MOVIE_ID + " INT UNIQUE, " + Constants.COLUMN_HEADER_TITLE + " VARCHAR, " + Constants.COLUMN_HEADER_RELEASE_DATE + " VARCHAR, " + Constants.COLUMN_HEADER_POSTER_PATH + " VARCHAR, " + Constants.COLUMN_HEADER_VOTE_AVERAGE + " FLOAT, " + Constants.COLUMN_HEADER_OVERVIEW + " VARCHAR);");
    }


    public void insertMovie(MovieObject movieObject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_HEADER_MOVIE_ID, movieObject.getId());
        contentValues.put(Constants.COLUMN_HEADER_TITLE, movieObject.getTitle());
        contentValues.put(Constants.COLUMN_HEADER_RELEASE_DATE, movieObject.getRelease_date());
        contentValues.put(Constants.COLUMN_HEADER_POSTER_PATH, movieObject.getPoster_path());
        contentValues.put(Constants.COLUMN_HEADER_VOTE_AVERAGE, movieObject.getVote_average());
        contentValues.put(Constants.COLUMN_HEADER_OVERVIEW, movieObject.getOverview());
        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public ArrayList<MovieObject> getFavoriteMovies() {
        ArrayList<MovieObject> list = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME + ";", null);
        while (result.moveToNext()) {
            MovieObject movieObject = new MovieObject();
            movieObject.setId(result.getInt(result.getColumnIndex(Constants.COLUMN_HEADER_MOVIE_ID)));
            movieObject.setTitle(result.getString(result.getColumnIndex(Constants.COLUMN_HEADER_TITLE)));
            movieObject.setRelease_date(result.getString(result.getColumnIndex(Constants.COLUMN_HEADER_RELEASE_DATE)));
            movieObject.setPoster_path(result.getString(result.getColumnIndex(Constants.COLUMN_HEADER_POSTER_PATH)));
            movieObject.setVote_average(result.getFloat(result.getColumnIndex(Constants.COLUMN_HEADER_POSTER_PATH)));
            movieObject.setOverview(result.getString(result.getColumnIndex(Constants.COLUMN_HEADER_OVERVIEW)));
            list.add(movieObject);
        }
        return list;
    }

    public boolean isFavoriteMovie(long id){
        Cursor result = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.COLUMN_HEADER_MOVIE_ID + " = " + id + ";", null);
        int count = 0;
        while (result.moveToNext()){
            return true;
        }
        return false;
    }

    public boolean deleteMovie(long id){
        return db.delete("Movie", Constants.COLUMN_HEADER_MOVIE_ID + "= " + id,null)>0;
    }

}
