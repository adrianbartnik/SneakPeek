package de.sneak.sneakpeek.data;


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.R.attr.id;
import static de.sneak.sneakpeek.data.MovieContract.MoviePredictionEntry.CONTENT_URI;


public class MovieDataProvider extends ContentProvider {

    static final int MOVIE_PREDICTIONS = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDBHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE_PREDICTIONS, MOVIE_PREDICTIONS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_PREDICTIONS:
                return MovieContract.MoviePredictionEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
//            case WEATHER_WITH_LOCATION_AND_DATE: {
//                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
//                break;
//            }
//            // "weather/*"
//            case WEATHER_WITH_LOCATION: {
//                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
//                break;
//            }
//            // "weather"
//            case WEATHER: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        WeatherContract.WeatherEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
            // "location"
            case MOVIE_PREDICTIONS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MoviePredictionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
//            case WEATHER: {
//                normalizeDate(values);
//                long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
//                if (_id > 0)
//                    returnUri = WeatherContract.WeatherEntry.buildWeatherUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
//            case LOCATION: {
//                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
//                if (_id > 0)
//                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
            case MOVIE_PREDICTIONS:
                long _id = db.insert(MovieContract.MoviePredictionEntry.TABLE_NAME, null, values);

                returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
//            case WEATHER:
//                rowsDeleted = db.delete(
//                        WeatherContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            case LOCATION:
//                rowsDeleted = db.delete(
//                        WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
//                break;
            case MOVIE_PREDICTIONS:
                rowsDeleted = db.delete(MovieContract.MoviePredictionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
//            case WEATHER:
//                normalizeDate(values);
//                rowsUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            case LOCATION:
//                rowsUpdated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
            case MOVIE_PREDICTIONS:
                rowsUpdated = db.update(MovieContract.MoviePredictionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

//    @Override
//    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        switch (match) {
//            case WEATHER:
//                db.beginTransaction();
//                int returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        normalizeDate(value);
//                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }
//    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}