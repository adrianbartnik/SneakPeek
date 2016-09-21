package de.sneak.sneakpeek.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {

    private static final String PREF_NAME = "SneakPeekSharedPref";
    private static final String PREF_LIGHT_THEME = "lightTheme";

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    public static <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return restAdapter.create(clazz);
    }

    public static void SetUseDarkTheme(Context context, boolean useDark) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putBoolean(PREF_LIGHT_THEME, useDark).apply();
    }

    public static boolean GetUseLightTheme(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(PREF_LIGHT_THEME, true);
    }
}
