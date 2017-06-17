package de.sneakpeek.data

data class Prediction(val week: String, val movies: List<MoviePrediction> = ArrayList<MoviePrediction>())

data class MoviePrediction(val title: String, val position: kotlin.Int)

data class ActualMovie(val title: String, val week: String) {
    fun formatWeek() : MovieWeek {
        val split = week.split("-")

        if (split.size == 2) {
            return MovieWeek(split[1].toInt(), split[0].toInt())
        } else {
            return MovieWeek(-1, -1)
        }
    }
}

data class MovieWeek(val week: Int, val year: Int)

data class PredictedStudios(val movieTitle: String, val studios: List<String>)

data class StudioPredictions(val studioTitle: String, val movies: List<String>)