package de.sneakpeek.service

import android.support.test.InstrumentationRegistry.getInstrumentation
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BackendParserTest {

    var backendParser: BackendParser = BackendParser()

    @Before
    fun before() {
        backendParser = BackendParser()
    }

    @Test
    fun parseActualMovies() {
        val backendResponse = loadAsset("actual.txt")

        val parseActualMovies = backendParser.parseActualMovies(response = backendResponse)

        assertEquals(37, parseActualMovies.size)

        val firstWeek = parseActualMovies[0]
        assertEquals("2016-36", firstWeek.week)
        assertEquals("Absolutely Fabulous: Der Film", firstWeek.title)

        val lastWeek = parseActualMovies[parseActualMovies.lastIndex]
        assertEquals("2017-20", lastWeek.week)
        assertEquals("Bevor du stirbst, zieht dein ganzes Leben an dir vorbei, sagen sie", lastWeek.title)
    }

    @Test
    fun parseStudios() {
        val backendResponse = loadAsset("studios.txt")

        val parsedStudios = backendParser.parseStudios(response = backendResponse)

        assertEquals(160, parsedStudios.size)
        assertEquals("Sausage Party - Es geht um die Wurst", parsedStudios[0].movieTitle)
        assertEquals(4, parsedStudios[0].studios.size)
        assertEquals(listOf("DSony Pictures Releasing", "SAnnapurna Pictures", "SColumbia Pictures", "SPoint Grey Pictures"), parsedStudios[0].studios)
        
        println(parsedStudios.size)
        println(parsedStudios)
    }

    @Test
    fun parsePrediction() {
        val backendResponse = loadAsset("progno.txt")

        val parsedPrediction = backendParser.parsePrediction(response = backendResponse)

        assertEquals(37, parsedPrediction.size)

        var prediction = parsedPrediction[0]

        assertEquals("2016-36", prediction.week)
        assertEquals(15, prediction.movies.size)
        assertEquals("Nerve", prediction.movies[0].title)
        assertEquals("Ab in den Dschungel", prediction.movies[prediction.movies.lastIndex].title)

        prediction = parsedPrediction[parsedPrediction.lastIndex]

        assertEquals("2017-20", prediction.week)
        assertEquals(12, prediction.movies.size)
        assertEquals("Bevor du stirbst, zieht dein ganzes Leben an dir vorbei, sagen sie", prediction.movies[0].title)
        assertEquals("VorwÃ¤rts immer!", prediction.movies[prediction.movies.lastIndex].title)
    }

    private fun loadAsset(name: String) : String{
        return getInstrumentation().context.resources.assets.open(name).bufferedReader().use { it.readText() }
    }
}