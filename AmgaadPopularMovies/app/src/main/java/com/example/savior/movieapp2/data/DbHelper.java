package com.example.savior.movieapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by savior on 6/22/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_NAME = "movie.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " + Contract.FavouriteMoviesEntry.TABLE_NAME + " (" +
                Contract.FavouriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Contract.FavouriteMoviesEntry.COLUMN_MOVIE_ID+ " LONG NOT NULL UNIQUE, " +
                Contract.FavouriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                Contract.FavouriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
              //  Contract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                Contract.FavouriteMoviesEntry.COLUMN_RATING + " REAL NOT NULL);";
                db.execSQL(SQL_CREATE_FAVOURITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.FavouriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
