package de.sneakpeek.test

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import de.sneakpeek.data.MovieInfo
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals


class GsonTest {

    private var gson: Gson = Gson();

    @Before
    fun before() {
        gson = GsonBuilder().serializeNulls().create()
    }

    @Test
    fun testGsonDeserialization() {

        val json = "{name:'Name', age:3}"

        val deserialization = gson.fromJson(json, Deserialization::class.java)

        assertEquals(Deserialization("Name", 3), deserialization)
    }

    @Test
    fun testGsonDeserializationAlsoSuccessful() {

        val json = "{name:'Name', age:3, location:'Here'}"

        val deserialization = gson.fromJson(json, Deserialization::class.java)

        assertEquals(Deserialization("Name", 3), deserialization)
    }

    @Test
    fun testGsonDeserializationAlsoSuccessfulNoFieldGiven() {

        val json = "{name:'Name'}"

        val deserialization = gson.fromJson(json, Deserialization::class.java)

        assertEquals(Deserialization("Name", 0), deserialization)
    }

    @Test
    fun testGsonSerialization() {

        val deserialization = gson.toJson(Deserialization("Name", 3))

        assertEquals("{\"name\":\"Name\",\"age\":3}", deserialization)
    }

    @Test
    fun testResponse() {

        val deserialization = gson.fromJson(readJson("response.json"), MovieInfo::class.java)

        assertEquals(327528, deserialization.id)
        assertEquals("Jazz legend Chet Baker finds love and redemption when he stars in a movie about his own troubled life to mount a comeback.", deserialization.overview)
        assertEquals(15, deserialization.credits?.cast?.size)
    }

    private fun readJson(name: String): String {
        val classLoader = javaClass.classLoader
        val resource = classLoader.getResource(name)
        return File(resource.path).readText()
    }

    data class Deserialization(@Expose @SerializedName("name") var name: String, @Expose @SerializedName("age") val age: Int)
}