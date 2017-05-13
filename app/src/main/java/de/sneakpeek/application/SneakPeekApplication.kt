package de.sneakpeek.application


import android.app.Application

import com.facebook.stetho.Stetho

class SneakPeekApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
