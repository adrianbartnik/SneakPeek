package de.sneakpeek.data

data class MoviePrediction(val title: String, val studios: List<String>) {
    constructor(title: String) : this(title, ArrayList<String>())
}

data class Prediction(val week: String, val movies: List<MoviePred>)

data class MoviePred(val title: String, val position: kotlin.Int)

data class ActualMovie(val title: String, val week: String)

data class PredictedStudios(val movieTitle: String, val studios: List<String>)

data class StudioPredictions(val studioTitle: String, val movies: List<String>)