package de.sneak.sneakpeek.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.sneak.sneakpeek.data.MovieContract.MoviePredictionEntry;
import de.sneak.sneakpeek.data.MovieContract.PastMovieEntry;
import de.sneak.sneakpeek.data.MovieContract.StudioEntry;

/**
 * Manages a local database for movie data.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "sneakpeek.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + MoviePredictionEntry.TABLE_NAME + " (" +
                MoviePredictionEntry._ID + " INTEGER PRIMARY KEY," +
                MoviePredictionEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, );";

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + StudioEntry.TABLE_NAME + " (" +

                StudioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                StudioEntry.COLUMN_NAME + " TEXT NOT NULL, " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviePredictionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StudioEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PastMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
