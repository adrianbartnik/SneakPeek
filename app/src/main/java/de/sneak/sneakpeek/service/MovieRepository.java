package de.sneak.sneakpeek.service;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.sneak.sneakpeek.data.Movie;
import de.sneak.sneakpeek.data.Score11Movie;
import de.sneak.sneakpeek.util.Util;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private static MovieRepository INSTANCE;

    private static PredictionService predictionService;
    private static OMDBService omdbService;

    private static List<String> moviePredictions;
    private static List<String> previousMovies;
    private static List<Score11Movie> moviesWithStudios;
    private static Map<String, Movie> movieInformation = new HashMap<>();


    private MovieRepository() {

        predictionService = Util.createRetrofitService(PredictionService.class, PredictionService.BASE_URL);

        omdbService = Util.createRetrofitService(OMDBService.class, OMDBService.BASE_URL);
    }

    public static MovieRepository getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new MovieRepository();
        }

        return INSTANCE;
    }

    public Observable<List<Score11Movie>> fetchStudios() {

        if (moviesWithStudios != null) {
            return Observable.just(moviesWithStudios);
        }

        return predictionService.getStudios()
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, List<Score11Movie>>() {
                    @Override
                    public List<Score11Movie> call(ResponseBody responseBody) {

                        List<Score11Movie> moviesWithStudios = new ArrayList<>(15);

                        try {
                            String response = responseBody.string();

                            Log.d(TAG, "Response: " + response);
                            String[] split = response.split((System.getProperty("line.separator")));

                            for (String movie: split) {
                                String[] movieWithStudio = movie.split("###");

                                Score11Movie score11Movie = new Score11Movie(movieWithStudio[0]);

                                for (int i = 1; i < movieWithStudio.length; i++) {
                                    score11Movie.studios.add(movieWithStudio[i].substring(1));
                                }

                                moviesWithStudios.add(score11Movie);
                            }

                            return moviesWithStudios;

                        } catch (IOException exception) {
                            Log.e(TAG, "Failed to decode response", exception);
                            return moviesWithStudios;
                        }
                    }})
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<Score11Movie>>() {
                    @Override
                    public void call(List<Score11Movie> score11MovieWithStudioses) {
                        moviesWithStudios = score11MovieWithStudioses;
                    }
                });
    }

    public Observable<List<String>> fetchPreviousMovies() {
        if (previousMovies != null) {
            return Observable.just(previousMovies);
        }

        return predictionService.getPreviousMovies()
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, List<String>>() {
                    @Override
                    public List<String> call(ResponseBody responseBody) {
                        String currentWeek = "No Response";

                        try {
                            String response = responseBody.string();

                            Log.d(TAG, "Response: " + response);
                            String[] split = response.split((System.getProperty("line.separator")));

                            List<String> previousMovies = new ArrayList<>();

                            for (String movie: split) {
                                previousMovies.add(movie.split("###")[1]);
                            }

                            return previousMovies;

                        } catch (IOException exception) {
                            Log.e(TAG, "Failed to decode response", exception);
                            return Collections.singletonList(currentWeek);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        previousMovies = list;
                    }
                });
    }

    public Observable<List<String>> fetchMovies() {

        if (moviePredictions != null) {
            return Observable.just(moviePredictions);
        }

        return predictionService.getPredictions()
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody responseBody) {
                        String currentWeek;

                        try {
                            String response = responseBody.string();

                            Log.d(TAG, "Response: " + response);
                            currentWeek = response.substring(0, response.indexOf(System.getProperty("line.separator")));

                            String[] weekAndMovies = currentWeek.split("###");

                            return Observable.from(Arrays.asList(Arrays.copyOfRange(weekAndMovies, 1, weekAndMovies.length)));

                        } catch (IOException exception) {
                            Log.e(TAG, "Failed to decode response", exception);
                            return Observable.empty();
                        }
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String title) {
                        return title.substring(title.indexOf("-") + 1).trim();
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        moviePredictions = list;
                    }
                });
    }

    public Observable<Movie> fetchFullMovieInformation(final String title) {

        if (movieInformation.containsKey(title)) {
            return Observable.just(movieInformation.get(title));
        }

        return omdbService.getMovie(title)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Movie>() {
                    @Override
                    public void call(Movie movie) {
                        movieInformation.put(title, movie);
                    }
                });
    }
}
