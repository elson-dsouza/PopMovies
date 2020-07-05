package com.example.elson.popmovies

import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.dagger.app.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm

open class MoviesApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
        AppInjector.appComponent = appComponent
        return appComponent
    }

}