package de.sneakpeek.util

import android.content.Context
import com.google.gson.GsonBuilder
import de.sneakpeek.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Util {

    private val PREF_NAME = "SneakPeekSharedPref"
    private val PREF_LIGHT_THEME = "lightTheme"

    fun <T> createRetrofitService(clazz: Class<T>, endPoint: String): T {

        val httpClient = OkHttpClient.Builder()

        val gson = GsonBuilder().serializeNulls().create()

        val restAdapter = Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))

        addApiKey(httpClient)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(logging)
        }

        return restAdapter
                .client(httpClient.build())
                .build()
                .create(clazz)
    }

    fun addApiKey(httpClient: OkHttpClient.Builder) {

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY)
                    .build()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder().url(url)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    fun SetUseDarkTheme(context: Context, useDark: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(PREF_LIGHT_THEME, useDark).apply()
    }

    fun GetUseLightTheme(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(PREF_LIGHT_THEME, true)
    }
}
