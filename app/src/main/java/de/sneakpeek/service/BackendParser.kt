package de.sneakpeek.service

import android.util.Log
import de.sneakpeek.data.ActualMovie
import de.sneakpeek.data.MoviePrediction
import de.sneakpeek.data.PredictedStudios
import de.sneakpeek.data.Prediction
import java.util.*

class BackendParser {
    fun parseActualMovies(response: String): List<ActualMovie> {

        Log.d(TAG, "Received response: $response")

        return response
                .split(System.getProperty("line.separator"))
                .map { it.trim() }
                .filter { !it.isEmpty() }
                .map {it.split("###") } // Splits Date
                .map { ActualMovie(week = it[0], title = it[1]) }
                .toList()
    }

    fun parseStudios(response: String): List<PredictedStudios> {

        Log.d(TAG, "Received response: $response")

        return response
                .split(System.getProperty("line.separator"))
                .map { it.trim() }
                .filter { !it.isEmpty() }
                .map { it.split("###") }
                .map { PredictedStudios(it[0], it.subList(1, it.size)) }
                .toList()
    }

    fun parsePrediction(response: String): List<Prediction> {

        val predictions = ArrayList<Prediction>(30)

        Log.d(TAG, "Received response: $response")

        val allPredictions = response
                .split(System.getProperty("line.separator"))
                .map { it.trim() }
                .filter { !it.isEmpty() }

        for (prediction in allPredictions) {

            val movieWithStudiosSplit = prediction.split("###")

            val positionOfSpace = movieWithStudiosSplit[0].indexOf(" ")
            val week = movieWithStudiosSplit[0]
                    .substring(positionOfSpace + 1)

            val movies = movieWithStudiosSplit
                    .subList(1, movieWithStudiosSplit.size)
                    .map { it.split("-") }
                    .map { MoviePrediction(it.get(1), it[0].toInt()) }

            predictions.add(Prediction(week, movies = movies))
        }

        return predictions
    }

    companion object {
        val TAG = BackendParser::class.simpleName
    }
}
