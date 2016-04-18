package com.example.shiv.movie.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;

/**
 * Created by shiv on 14/4/16.
 */

@ContentProvider(authority = MovieObjectContentProvider.AUTHORITY, database = MovieObjectDatabase.class)
public class MovieObjectContentProvider {

    public static final String AUTHORITY = "com.example.shiv.movie.database.MovieObjectContentProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

}

