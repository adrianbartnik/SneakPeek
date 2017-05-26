package de.sneakpeek.service

import de.sneakpeek.data.Movie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBService {

    @GET("?&y=&plot=full&r=json")
    fun getMovie(@Query("t") title: String): Observable<Movie>

    companion object {
        val BASE_URL = "http://www.omdbapi.com/"
    }
}
