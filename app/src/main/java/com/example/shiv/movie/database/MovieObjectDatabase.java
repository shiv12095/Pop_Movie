package com.example.shiv.movie.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by shiv on 14/4/16.
 */

@Database(version = MovieObjectDatabase.VERSION)
public class MovieObjectDatabase {

    public static final int VERSION = 1;

    @Table(MovieObjectColumns.class) public static final String MovieObject = "movies";
}
