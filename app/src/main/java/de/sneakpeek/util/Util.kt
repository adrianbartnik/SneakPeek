package de.sneakpeek.util

import android.content.Context
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Util {

    private val PREF_NAME = "SneakPeekSharedPref"
    private val PREF_LIGHT_THEME = "lightTheme"

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

    fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {
        val restAdapter = Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return restAdapter.create(clazz)
    }

    fun SetUseDarkTheme(context: Context, useDark: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(PREF_LIGHT_THEME, useDark).apply()
    }

    fun GetUseLightTheme(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(PREF_LIGHT_THEME, true)
    }
}
