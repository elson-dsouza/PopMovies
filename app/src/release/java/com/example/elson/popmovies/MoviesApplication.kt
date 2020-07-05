package com.example.elson.popmovies

import com.example.elson.popmovies.dagger.app.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm

class MoviesApplication : DaggerApplication() {

    private val applicationInjector = AppInjector.buildAppComponent(this)

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationInjector
    }

}