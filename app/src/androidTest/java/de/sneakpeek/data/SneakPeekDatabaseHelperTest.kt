package de.sneakpeek.data

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SneakPeekDatabaseHelperTest {

    private var databaseHelper: SneakPeekDatabaseHelper? = null

    @Before
    fun setUp() {
        databaseHelper = SneakPeekDatabaseHelper.GetInstance(InstrumentationRegistry.getTargetContext())
        databaseHelper?.clear()
    }

    @After
    fun after() {
        databaseHelper?.clear()
    }

    fun getTestProgno(): List<Prediction> {
        val predictions = ArrayList<Prediction>()

        var movies = ArrayList<MoviePrediction>()
        (0..3).mapTo(movies) { MoviePrediction("Title$it", it) }
        predictions.add(Prediction("2017-01", movies))

        movies = ArrayList<MoviePrediction>()
        (0..4).mapTo(movies) { MoviePrediction("Title$it", it) }
        predictions.add(Prediction("2017-02", movies))

        movies = ArrayList<MoviePrediction>()
        (1..15).mapTo(movies) { MoviePrediction("New-Title$it", it) }
        predictions.add(Prediction("2017-03", movies))

        return predictions
    }

    fun getTestStudios(): List<PredictedStudios> {
        val predictedStudios = ArrayList<PredictedStudios>()

        var studios = ArrayList<String>()
        (0..3).mapTo(studios) { "Studio-$it" }
        predictedStudios.add(PredictedStudios("Title1", studios))

        studios = ArrayList<String>()
        (0..2).mapTo(studios) { "Studio-$it" }
        predictedStudios.add(PredictedStudios("Title2", studios))

        studios = ArrayList<String>()
        (0..2).mapTo(studios) { "New-Studio-$it" }
        predictedStudios.add(PredictedStudios("Title3", studios))

        return predictedStudios
    }

    fun getActualMovies(): List<ActualMovie> {
        val actualMovies = ArrayList<ActualMovie>()

        (0..20).mapTo(actualMovies) { ActualMovie("Title$it", "Week-$it") }

        return actualMovies
    }

    @Test
    fun insertSameMovies() {
        databaseHelper?.insertMoviePredictions(getTestProgno())

        var prediction = databaseHelper?.getPredictionForWeek("2017-01")

        assertEquals(4, prediction?.size)
        for (i in 0..3) {
            assertEquals("Title$i", prediction?.get(i))
        }

        prediction = databaseHelper?.getPredictionForWeek("2017-02")

        assertEquals(5, prediction?.size)
        for (i in 0..4) {
            assertEquals("Title$i", prediction?.get(i))
        }

        val movies = databaseHelper?.getMovies()

        assertEquals(20, movies?.size)
    }

    @Test
    fun retrieveAcrossTwoPredictions() {
        databaseHelper?.insertMoviePredictions(getTestProgno())

        var prediction = databaseHelper?.getPredictionForWeek("2017-01")

        assertEquals(true, prediction?.contains("Title1"))

        prediction = databaseHelper?.getPredictionForWeek("2017-02")

        assertEquals(true, prediction?.contains("Title1"))
    }

    @Test
    fun testInsertIdempotent() {
        databaseHelper?.insertMoviePredictions(getTestProgno())

        var prediction = databaseHelper?.getPredictionForWeek("2017-01")
        assertEquals(4, prediction?.size)

        var movies = databaseHelper?.getMovies()
        assertEquals(20, movies?.size)

        databaseHelper?.insertMoviePredictions(getTestProgno())

        prediction = databaseHelper?.getPredictionForWeek("2017-01")
        assertEquals(4, prediction?.size)

        movies = databaseHelper?.getMovies()
        assertEquals(20, movies?.size)
    }

    @Test
    fun getPredictionForCurrentWeek() {
        databaseHelper?.insertMoviePredictions(getTestProgno())

        val prediction = databaseHelper?.getMoviePrediction()

        assertEquals("2017-03", prediction?.week)

        assertEquals(15, prediction?.movies?.size)

        (1..15).forEach { assertEquals("New-Title$it", prediction?.movies?.get(it - 1)?.title) }
    }

    @Test
    fun insertStudios() {
        databaseHelper?.insertStudios(getTestStudios())

        val studios = databaseHelper?.getStudios()

        assertEquals(7, studios?.size)

        val movies = databaseHelper?.getMovies()

        assertEquals(3, movies?.size)
    }

    @Test
    fun insertStudiosIdempotent() {
        databaseHelper?.insertStudios(getTestStudios())

        var studios = databaseHelper?.getStudios()

        assertEquals(7, studios?.size)

        var movies = databaseHelper?.getMovies()

        assertEquals(3, movies?.size)

        databaseHelper?.insertStudios(getTestStudios())

        studios = databaseHelper?.getStudios()

        assertEquals(7, studios?.size)

        movies = databaseHelper?.getMovies()

        assertEquals(3, movies?.size)
    }

    @Test
    fun insertStudioPredictions() {
        databaseHelper?.insertStudios(getTestStudios())

        val prediction = databaseHelper?.getStudioPredictions()
        assertEquals(7, prediction?.size)

        var studio = prediction?.get(0)
        assertEquals("New-Studio-0", studio?.studioTitle)
        assertEquals(1, studio?.movies?.size)

        studio = prediction?.get(3)
        assertEquals("Studio-0", studio?.studioTitle)
        assertEquals(2, studio?.movies?.size)
    }

    @Test
    fun insertActualMovies() {
        databaseHelper?.insertActualMovies(getActualMovies())

        val prediction = databaseHelper?.getActualMovies()
        assertEquals(21, prediction?.size)

        (0..20).forEach { assertEquals(ActualMovie("Title$it", "Week-$it"), prediction?.get(it)) }
    }
}