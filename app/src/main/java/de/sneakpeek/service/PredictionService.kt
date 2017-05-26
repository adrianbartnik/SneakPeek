package de.sneakpeek.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface PredictionService {

    @get:GET("progno.txt")
    val moviePredictions: Observable<ResponseBody>

    @get:GET("studios.txt")
    val studios: Observable<ResponseBody>

    @get:GET("actual.txt")
    val actualMovies: Observable<ResponseBody>

    companion object {
        val BASE_URL = "http://lebkuchenbande.de/"
    }
}
