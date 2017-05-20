package de.sneakpeek.service

import android.content.Context
import de.sneakpeek.data.*
import de.sneakpeek.util.Util
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MovieRepository private constructor(context: Context) {

    private val BACKEND_PARSER: BackendParser = BackendParser()
    private var database : SneakPeekDatabaseHelper = SneakPeekDatabaseHelper.GetInstance(context)

    fun getActualMovies() : Observable<List<ActualMovie>> {
        return predictionService.studios
                .subscribeOn(Schedulers.newThread())
                .map { it.toString() }
                .map { return@map BACKEND_PARSER.parseActualMovies(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { database.insertActualMovies(it) }
    }

    fun fetchStudios(): Observable<List<PredictedStudios>> {

        return predictionService.studios
                .subscribeOn(Schedulers.newThread())
                .map { it.toString() }
                .map { return@map BACKEND_PARSER.parseStudios(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { database.insertStudios(it) }
    }

    fun fetchMovies(): Observable<List<Prediction>> {

        return predictionService.studios
                .subscribeOn(Schedulers.newThread())
                .map { it.toString() }
                .map { return@map BACKEND_PARSER.parsePrediction(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { database.insertMoviePredictions(it) }
    }

    fun fetchFullMovieInformation(title: String): Observable<Movie> {

        return omdbService.getMovie(title)
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { movie -> movieInformation.put(title, movie) }
    }

    companion object {

        private var INSTANCE: MovieRepository? = null

        private val predictionService: PredictionService = Util.createRetrofitService(PredictionService::class.java, PredictionService.BASE_URL)
        private val omdbService: OMDBService = Util.createRetrofitService(OMDBService::class.java, OMDBService.BASE_URL)

        private val movieInformation = HashMap<String, Movie>()

        fun  getInstance(context: Context): MovieRepository {
            if (INSTANCE == null)
                INSTANCE = MovieRepository(context)

            return INSTANCE!!
        }
    }
}
