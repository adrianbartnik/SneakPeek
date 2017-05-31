package de.sneakpeek.service

import de.sneakpeek.data.MovieInfo
import de.sneakpeek.data.MovieQuery
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {

    @GET("search/movie?language=en-US&page=1&include_adult=true") // TODO Language
    fun queryMovie(@Query("query") title: String): Observable<MovieQuery>

    @GET("movie/{id}?language=en-US&append_to_response=credits")
    fun getFullMovieInfo(@Path("id") id: Int): Observable<MovieInfo>

    companion object {
        val BASE_URL = "https://api.themoviedb.org/3/"
    }
}
