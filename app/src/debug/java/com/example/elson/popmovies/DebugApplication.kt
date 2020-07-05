package com.example.elson.popmovies

import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.dagger.app.DaggerDebugAppComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DebugApplication: MoviesApplication() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerDebugAppComponent.builder()
                .application(this)
                .build()
        AppInjector.appComponent = appComponent
        return appComponent
    }
}