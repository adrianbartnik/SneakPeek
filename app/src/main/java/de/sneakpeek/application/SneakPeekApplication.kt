package de.sneakpeek.application


import android.app.Application
import com.facebook.stetho.Stetho
import de.sneakpeek.BuildConfig

class SneakPeekApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}
