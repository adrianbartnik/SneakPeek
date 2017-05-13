package de.sneakpeek.service

import okhttp3.ResponseBody
import retrofit2.http.GET
import rx.Observable

interface PredictionService {

    @get:GET("progno.txt")
    val predictions: Observable<ResponseBody>

    @get:GET("studios.txt")
    val studios: Observable<ResponseBody>

    @get:GET("actual.txt")
    val previousMovies: Observable<ResponseBody>

    companion object {
        val BASE_URL = "http://lebkuchenbande.de/"
    }
}
