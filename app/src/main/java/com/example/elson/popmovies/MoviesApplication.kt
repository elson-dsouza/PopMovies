package com.example.elson.popmovies

import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.dagger.app.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class MoviesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.factory().create(this)
        AppInjector.appComponent = appComponent
        return appComponent
    }
}
