package de.sneakpeek.service

import android.content.Context
import de.sneakpeek.data.*
import de.sneakpeek.util.Util
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MovieRepository private constructor(context: Context) {

    private val BACKEND_PARSER: BackendParser = BackendParser()
    private var database : SneakPeekDatabaseHelper = SneakPeekDatabaseHelper.GetInstance(context)

    private fun getActualMovies() : Observable<List<ActualMovie>> {

        return predictionService.actualMovies
                .subscribeOn(Schedulers.newThread())
                .map { it.string() }
                .map { return@map BACKEND_PARSER.parseActualMovies(it) }
                .onErrorResumeNext { _: Throwable? -> Observable.empty() }
                .doOnNext { database.insertActualMovies(it) }
    }

    fun getActual() : Maybe<List<ActualMovie>>{
        return Observable
                .concat(getActualMovies(), Observable.just(database.getActualMovies()))
                .subscribeOn(Schedulers.newThread())
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchStudios(): Observable<List<PredictedStudios>> {

        return predictionService.studios
                .subscribeOn(Schedulers.newThread())
                .map { it.string() }
                .map { return@map BACKEND_PARSER.parseStudios(it) }
                .onErrorResumeNext { _: Throwable? -> Observable.empty() }
                .doOnNext { database.insertStudios(it) }
    }

    fun getStudios(): Maybe<List<StudioPredictions>>? {

        val pairedStudios = fetchStudios()
                .flatMap { Observable.fromIterable(it) }
                .flatMap { (movieTitle, studios) -> Observable.fromIterable(studios).map { Pair(it, movieTitle) } }
                .toList()
                .blockingGet()

        val groupedStudios = pairedStudios.groupBy { it.first }.map { StudioPredictions(it.key, it.value.map { it.second }) }

        if (groupedStudios.isEmpty()) {
            return Observable.just(database.getStudioPredictions())
                    .subscribeOn(Schedulers.newThread())
                    .firstElement()
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            return Observable.just(groupedStudios)
                    .subscribeOn(Schedulers.newThread())
                    .firstElement()
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun getMovies(): Maybe<Prediction> {
        return Observable
                .concat(fetchMovies(), Observable.just(database.getMoviePrediction()))
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchMovies(): Observable<Prediction> {

        return predictionService.moviePredictions
                .subscribeOn(Schedulers.newThread())
                .map { it.string() }
                .map { return@map BACKEND_PARSER.parsePrediction(it) }
                .onErrorResumeNext { _: Throwable? -> Observable.empty() }
                .doOnNext { database.insertMoviePredictions(it) }
                .map { it.last() }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchFullMovieInformation(title: String): Maybe<MovieInfo> {

        if (movieInformation.containsKey(title)) {
            return Observable.just(movieInformation[title]!!).firstElement()
        }

        return movieService.queryMovie(title)
                .timeout(10, TimeUnit.SECONDS)
                .filter { it.results != null }
                .flatMap { movieService.getFullMovieInfo(it.results!![0].id)}
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { movieInformation.put(title, it) }
                .firstElement()
    }

    companion object {

        private var INSTANCE: MovieRepository? = null

        private val predictionService: PredictionService = Util.createRetrofitService(PredictionService::class.java, PredictionService.BASE_URL)
        private val movieService: TheMovieDBService = Util.createRetrofitService(TheMovieDBService::class.java, TheMovieDBService.BASE_URL)

        private val movieInformation = HashMap<String, MovieInfo>()

        fun  getInstance(context: Context): MovieRepository {
            if (INSTANCE == null)
                INSTANCE = MovieRepository(context)

            return INSTANCE!!
        }
    }
}
