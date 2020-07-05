package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import com.example.elson.popmovies.dagger.authentication.AuthenticationSubComponent

object AppInjector {

    lateinit var appComponent: AppComponent
        private set

    lateinit var authenticationSubComponent: AuthenticationSubComponent
        private set

    @Synchronized
    fun buildAppComponent(application: MoviesApplication): AppComponent {
        appComponent = DaggerAppComponent.builder()
                .application(application)
                .build()
        return appComponent
    }

    @Synchronized
    fun buildLoginSubComponent(): AuthenticationSubComponent {
        authenticationSubComponent = appComponent.authenticationSubcomponentBuilder.build()
        return authenticationSubComponent
    }
}