package de.sneak.sneakpeek.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

public interface PredictionService {

    String BASE_URL = "http://lebkuchenbande.de/";

    @GET("progno.txt")
    Observable<ResponseBody> getPredictions();

    @GET("studios.txt")
    Observable<ResponseBody> getStudios();

    @GET("actual.txt")
    Observable<ResponseBody> getPreviousMovies();
}
