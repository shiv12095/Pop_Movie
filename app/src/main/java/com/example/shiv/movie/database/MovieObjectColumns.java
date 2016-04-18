package com.example.shiv.movie.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import org.joda.time.DateTime;

/**
 * Created by shiv on 14/4/16.
 */
public interface MovieObjectColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String ID = "id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.REAL)
    @NotNull
    public static final String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String OVERVIEW = "overview";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String POSTER = "poster";
}
