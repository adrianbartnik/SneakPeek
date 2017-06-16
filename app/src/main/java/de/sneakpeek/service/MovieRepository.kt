package de.sneakpeek.service

import android.content.Context
import de.sneakpeek.data.*
import de.sneakpeek.util.Util
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleFromUnsafeSource
import io.reactivex.schedulers.Schedulers
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class MovieRepository constructor(context: Context) {

    private val database: SneakPeekDatabaseHelper by lazy { SneakPeekDatabaseHelper.GetInstance(context) }

    private fun getActualMovies(): Observable<List<ActualMovie>> {

        return predictionService.actualMovies
                .subscribeOn(Schedulers.io())
                .map { InputStreamReader(it.byteStream(), Charset.forName("windows-1252")).readText() }
                .map { return@map BACKEND_PARSER.parseActualMovies(it) }
                .onErrorResumeNext { _: Throwable? -> Observable.empty() }
                .doOnNext { database.insertActualMovies(it) }
    }

    fun getActual(): Maybe<List<ActualMovie>> {
        return Observable
                .concat(getActualMovies(), Observable.just(database.getActualMovies()))
                .subscribeOn(Schedulers.io())
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchStudios(): Observable<List<PredictedStudios>> {

        return predictionService.studios
                .subscribeOn(Schedulers.io())
                .map { it.string() }
                .map { return@map BACKEND_PARSER.parseStudios(it) }
                .doOnNext { database.insertStudios(it) }
    }

    fun getStudios(): Single<List<StudioPredictions>>? {

        return fetchStudios()
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .flatMap { (movieTitle, studios) -> Observable.fromIterable(studios).map { Pair(it, movieTitle) } }
                .groupBy { it.first }
                .flatMap { Observable.just(Pair(it.key, it.map { it.second }.reduce { t1: String?, t2: String? -> "$t1####$t2" })) }
                .toList()
                .map { it.map { StudioPredictions(it.first, it.second.blockingGet().split("####")) } }
                .onErrorResumeNext { SingleFromUnsafeSource.just(database.getStudioPredictions()) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMovies(): Maybe<Prediction> {
        return Observable
                .concat(fetchMovies(), Observable.just(database.getMoviePrediction()))
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun fetchMovies(): Observable<Prediction> {

        return predictionService.moviePredictions
                .subscribeOn(Schedulers.io())
                .map { InputStreamReader(it.byteStream(), Charset.forName("windows-1252")).readText() }
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
                .flatMap { movieService.getFullMovieInfo(it.results!![0].id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { movieInformation.put(title, it) }
                .firstElement()
    }

    companion object {

        private val BACKEND_PARSER: BackendParser by lazy { BackendParser() }

        private val predictionService: PredictionService by lazy { Util.createRetrofitService(PredictionService::class.java, PredictionService.BASE_URL) }
        private val movieService: TheMovieDBService by lazy { Util.createRetrofitService(TheMovieDBService::class.java, TheMovieDBService.BASE_URL) }

        private val movieInformation = HashMap<String, MovieInfo>()
    }
}
