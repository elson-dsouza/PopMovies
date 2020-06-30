package com.example.elson.popmovies

import android.app.Application
import io.realm.Realm

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}