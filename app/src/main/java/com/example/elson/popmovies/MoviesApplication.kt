package com.example.elson.popmovies

import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.dagger.app.DaggerAppComponent
import com.example.elson.popmovies.data.SecurePrefs
import com.example.elson.popmovies.data.initRealmDb
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

open class MoviesApplication : DaggerApplication() {

    @Inject lateinit var securePrefs: SecurePrefs

    override fun onCreate() {
        super.onCreate()
        initRealmDb(this, securePrefs)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.factory().create(this)
        AppInjector.appComponent = appComponent
        return appComponent
    }

}