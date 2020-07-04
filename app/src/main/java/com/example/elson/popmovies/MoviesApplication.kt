package com.example.elson.popmovies

import android.app.Application
import com.facebook.stetho.Stetho
import io.realm.Realm

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

}