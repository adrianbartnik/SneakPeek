package de.sneakpeek.data


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Provides persistence for all data retrieved from Armin´s server.
 * Sketch of database scheme can be found in root directory in Tables.md
 */
class SneakPeekDatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(context, SneakPeekDatabaseHelper.DATABASE_NAME, null, SneakPeekDatabaseHelper.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_MOVIES_TABLE = "CREATE TABLE $TABLE_MOVIES ($KEY_MOVIE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_MOVIE_TITLE TEXT NOT NULL UNIQUE)"

        val CREATE_STUDIOS_TABLE = "CREATE TABLE $TABLE_STUDIOS ($KEY_STUDIO_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_STUDIO_TITLE TEXT NOT NULL UNIQUE)"

        val CREATE_MOVIE_PREDICTIONS_TABLE = "CREATE TABLE $TABLE_MOVIE_PREDICTIONS ($KEY_WEEK TEXT NOT NULL, " +
                "$KEY_POSITION INTEGER, " +
                "$KEY_MOVIE_ID INTEGER, " +
                "FOREIGN KEY ($KEY_MOVIE_ID) REFERENCES $TABLE_MOVIES ($KEY_MOVIE_ID), " +
                "UNIQUE ($KEY_WEEK, $KEY_MOVIE_ID, $KEY_POSITION) ON CONFLICT REPLACE);"

        val CREATE_STUDIO_PREDICTION_TABLE = "CREATE TABLE $TABLE_STUDIO_PREDICTIONS (" +
                "$KEY_STUDIO_ID INTEGER, " +
                "$KEY_MOVIE_ID INTEGER, " +
                "FOREIGN KEY ($KEY_STUDIO_ID) REFERENCES $TABLE_STUDIOS ($KEY_STUDIO_ID), " +
                "FOREIGN KEY ($KEY_MOVIE_ID) REFERENCES $TABLE_MOVIES ($KEY_MOVIE_ID), " +
                "UNIQUE ($KEY_MOVIE_ID, $KEY_STUDIO_ID) ON CONFLICT REPLACE);"

        val CREATE_ACTUAL_MOVIES_TABLE = "CREATE TABLE $TABLE_ACTUAL_MOVIES ($KEY_WEEK TEXT NOT NULL, " +
                "$KEY_MOVIE_ID INTEGER, " +
                "FOREIGN KEY ($KEY_MOVIE_ID) REFERENCES $TABLE_MOVIES ($KEY_MOVIE_ID)," +
                " UNIQUE ($KEY_WEEK, $KEY_MOVIE_ID) ON CONFLICT REPLACE);"

        db.execSQL("PRAGMA foreign_keys = ON;")
        db.execSQL(CREATE_MOVIES_TABLE)
        db.execSQL(CREATE_STUDIOS_TABLE)
        db.execSQL(CREATE_MOVIE_PREDICTIONS_TABLE)
        db.execSQL(CREATE_STUDIO_PREDICTION_TABLE)
        db.execSQL(CREATE_ACTUAL_MOVIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDIOS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_PREDICTIONS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDIO_PREDICTIONS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTUAL_MOVIES)
        onCreate(db)
    }

    private fun getMovieID(db: SQLiteDatabase, title: String): Long {

        val query = "SELECT $KEY_MOVIE_ID FROM $TABLE_MOVIES WHERE $KEY_MOVIE_TITLE = ?"

        db.rawQuery(query, Array<String>(1, { title })).use {
            if (it.moveToNext()) {
                return it.getLong(0)
            } else {
                return -1
            }
        }
    }

    private fun insertMovieOrGetID(db: SQLiteDatabase, title: String): Long {

        val id = getMovieID(db, title)

        if (id != -1.toLong()) {
            return id
        } else {
            val item = ContentValues()
            item.put(KEY_MOVIE_TITLE, title)

            return db.insertWithOnConflict(TABLE_MOVIES, null, item, SQLiteDatabase.CONFLICT_ABORT)
        }
    }

    private fun getStudioID(db: SQLiteDatabase, title: String): Long {

        val query = "SELECT $KEY_STUDIO_ID FROM $TABLE_STUDIOS WHERE $KEY_STUDIO_TITLE = ?"

        db.rawQuery(query, Array<String>(1, { title })).use {
            if (it.moveToNext()) {
                return it.getLong(0)
            } else {
                return -1
            }
        }
    }

    private fun insertStudioOrGetID(db: SQLiteDatabase, title: String): Long {

        val id = getStudioID(db, title)

        if (id != -1.toLong()) {
            return id
        } else {
            val item = ContentValues()
            item.put(KEY_STUDIO_TITLE, title)

            return db.insertWithOnConflict(TABLE_STUDIOS, null, item, SQLiteDatabase.CONFLICT_ABORT)
        }
    }

    fun insertMoviePredictions(predictions: List<Prediction>) {

        val db = writableDatabase

        for ((week, movies) in predictions) {
            for ((title, position) in movies) {

                val id = insertMovieOrGetID(db, title)

                insertMoviePredictionIfNotPresent(db, id, position, week)
            }
        }
    }

    private fun insertMoviePredictionIfNotPresent(db: SQLiteDatabase, movieID : Long, position : Int, week: String) {
        val query = "SELECT * FROM $TABLE_MOVIE_PREDICTIONS " +
                "WHERE $KEY_MOVIE_ID = ? AND $KEY_POSITION = ? AND $KEY_WEEK = ?"

        db.rawQuery(query, arrayOf(movieID.toString(), position.toString(), week)).use {
            if (it.moveToNext()) {
                return
            } else {
                val item = ContentValues()
                item.put(KEY_MOVIE_ID, movieID)
                item.put(KEY_POSITION, position)
                item.put(KEY_WEEK, week)
                db.insertWithOnConflict(TABLE_MOVIE_PREDICTIONS, null, item, SQLiteDatabase.CONFLICT_REPLACE)
            }
        }
    }

    fun insertStudios(allStudios: List<PredictedStudios>) {

        val db = writableDatabase

        for ((movieTitle, studios) in allStudios) {

            val movieID = insertMovieOrGetID(db, movieTitle)

            for (studioTitle in studios) {

                val studioID = insertStudioOrGetID(db, studioTitle)

                insertStudioPredictionIfNotPresent(db, movieID, studioID)
            }
        }
    }

    private fun insertStudioPredictionIfNotPresent(db: SQLiteDatabase, movieID : Long, studioID : Long) {
        val query = "SELECT * FROM $TABLE_STUDIO_PREDICTIONS " +
                "WHERE $KEY_MOVIE_ID = ? AND $KEY_STUDIO_ID = ?"

        db.rawQuery(query, arrayOf(movieID.toString(), studioID.toString())).use {
            if (it.moveToNext()) {
                return
            } else {
                val item = ContentValues()
                item.put(KEY_MOVIE_ID, movieID)
                item.put(KEY_STUDIO_ID, studioID)
                db.insertWithOnConflict(TABLE_STUDIO_PREDICTIONS, null, item, SQLiteDatabase.CONFLICT_REPLACE)
            }
        }
    }

    fun insertActualMovies(actualMovies: List<ActualMovie>) {
        val db = writableDatabase

        for ((movieTitle, week) in actualMovies) {

            val movieID = insertMovieOrGetID(db, movieTitle)

            insertActualMovieIfNotPresent(db, movieID, week)
        }
    }

    private fun insertActualMovieIfNotPresent(db: SQLiteDatabase, movieID : Long, week: String) {
        val query = "SELECT * FROM $TABLE_ACTUAL_MOVIES " +
                "WHERE $KEY_MOVIE_ID = ? AND $KEY_WEEK = ?"

        db.rawQuery(query, arrayOf(movieID.toString(), week)).use {
            if (it.moveToNext()) {
                return
            } else {
                val item = ContentValues()
                item.put(KEY_MOVIE_ID, movieID)
                item.put(KEY_WEEK, week)

                db.insertWithOnConflict(TABLE_ACTUAL_MOVIES, null, item, SQLiteDatabase.CONFLICT_REPLACE)
            }
        }
    }

    fun getMovies(): List<String> {
        val db = readableDatabase

        val selectQuery = "SELECT $KEY_MOVIE_TITLE FROM $TABLE_MOVIES"

        db.rawQuery(selectQuery, null).use {

            val list = ArrayList<String>()

            val columnIndex = it.getColumnIndex(KEY_MOVIE_TITLE)

            while (it.moveToNext()) {
                val title = it.getString(columnIndex)
                list.add(title)
            }

            return list
        }
    }

    fun getStudios(): List<String> {
        val db = readableDatabase

        val selectQuery = "SELECT $KEY_STUDIO_TITLE FROM $TABLE_STUDIOS"

        db.rawQuery(selectQuery, null).use {

            val list = ArrayList<String>()

            val columnIndex = it.getColumnIndex(KEY_STUDIO_TITLE)

            while (it.moveToNext()) {
                val title = it.getString(columnIndex)
                list.add(title)
            }

            return list
        }
    }

    fun getActualMovies(): List<ActualMovie> {
        val db = readableDatabase

        val selectQuery = "SELECT $KEY_MOVIE_TITLE, $KEY_WEEK FROM $TABLE_ACTUAL_MOVIES a " +
                "INNER JOIN $TABLE_MOVIES m " +
                "ON a.$KEY_MOVIE_ID=m.$KEY_MOVIE_ID"

        db.rawQuery(selectQuery, null).use {

            val list = ArrayList<ActualMovie>()

            val columnIndexTitle = it.getColumnIndex(KEY_MOVIE_TITLE)
            val columnIndexWeek = it.getColumnIndex(KEY_WEEK)

            while (it.moveToNext()) {
                val title = it.getString(columnIndexTitle)
                val week = it.getString(columnIndexWeek)
                list.add(ActualMovie(title, week))
            }

            return list
        }
    }

    fun getPrediction(week: String): List<String> {
        val db = readableDatabase

        val movies = ArrayList<String>()

        val query = "SELECT m.$KEY_MOVIE_TITLE FROM $TABLE_MOVIES m " +
                "INNER JOIN $TABLE_MOVIE_PREDICTIONS p " +
                "ON m.$KEY_MOVIE_ID=p.$KEY_MOVIE_ID " +
                "WHERE p.$KEY_WEEK = ? " +
                "ORDER BY p.$KEY_WEEK asc"

        db.rawQuery(query, Array<String>(1, { week })).use {
            val columnIndex = it.getColumnIndex(KEY_MOVIE_TITLE)

            while (it.moveToNext()) {
                val title = it.getString(columnIndex)
                movies.add(title)
            }

            return movies
        }
    }

    fun getStudioPredictions(): List<StudioPredictions> {
        val db = readableDatabase

        val movies = ArrayList<StudioPredictions>()

        val query = "SELECT *, s.$KEY_STUDIO_TITLE, GROUP_CONCAT(m.$KEY_MOVIE_TITLE) as movies " +
                "FROM $TABLE_STUDIO_PREDICTIONS p " +
                "INNER JOIN $TABLE_STUDIOS s " +
                "ON p.$KEY_STUDIO_ID=s.$KEY_STUDIO_ID " +
                "INNER JOIN $TABLE_MOVIES m " +
                "ON p.$KEY_MOVIE_ID=m.$KEY_MOVIE_ID " +
                "GROUP BY s.$KEY_STUDIO_TITLE "
                "ORDER BY s.$KEY_STUDIO_TITLE"

        db.rawQuery(query, null).use {
            val columnIndexStudio = it.getColumnIndex(KEY_STUDIO_TITLE)
            val columnIndexMovies = it.getColumnIndex("movies")

            while (it.moveToNext()) {

                val title = it.getString(columnIndexStudio)
                val studios = it.getString(columnIndexMovies).split(",")

                movies.add(StudioPredictions(title, studios))
            }

            return movies
        }
    }

    fun clear() {
        writableDatabase.execSQL("delete from " + TABLE_MOVIES)
        writableDatabase.execSQL("delete from " + TABLE_STUDIOS)
        writableDatabase.execSQL("delete from " + TABLE_MOVIE_PREDICTIONS)
        writableDatabase.execSQL("delete from " + TABLE_STUDIO_PREDICTIONS)
        writableDatabase.execSQL("delete from " + TABLE_ACTUAL_MOVIES)
    }

    companion object {

        private val DATABASE_VERSION = 1

        private val DATABASE_NAME = "sneakpeek_db"

        private val TABLE_MOVIES = "movies"
        private val TABLE_STUDIOS = "studios"
        private val TABLE_MOVIE_PREDICTIONS = "movie_predictions"
        private val TABLE_STUDIO_PREDICTIONS = "studio_predictions"
        private val TABLE_ACTUAL_MOVIES = "actual_movies"

        private val KEY_MOVIE_ID = "movie_id"
        private val KEY_STUDIO_ID = "studio_id"
        private val KEY_MOVIE_TITLE = "movie_title"
        private val KEY_STUDIO_TITLE = "studio_title"
        private val KEY_POSITION = "position"
        private val KEY_WEEK = "week"

        private var INSTANCE: SneakPeekDatabaseHelper? = null

        fun GetInstance(context: Context): SneakPeekDatabaseHelper {

            if (INSTANCE == null) {
                INSTANCE = SneakPeekDatabaseHelper(context)
            }

            return INSTANCE!!
        }
    }
}