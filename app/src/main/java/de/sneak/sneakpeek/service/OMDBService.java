package de.sneak.sneakpeek.service;

import de.sneak.sneakpeek.data.Movie;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OMDBService {

    String BASE_URL = "http://www.omdbapi.com/";

    @GET("?&y=&plot=full&r=json")
    Observable<Movie> getMovie(@Query("t") String title);
}
