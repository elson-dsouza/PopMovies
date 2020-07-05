package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import com.example.elson.popmovies.dagger.authentication.AuthenticationSubComponent

object AppInjector {

    lateinit var appComponent: AppComponent
        private set

    private lateinit var authenticationSubComponent: AuthenticationSubComponent

    @Synchronized
    fun buildAppComponent(application: MoviesApplication): AppComponent {
        appComponent = DaggerAppComponent.builder()
                .application(application)
                .build()
        return appComponent
    }

    @Synchronized
    fun getAuthenticationSubComponent(): AuthenticationSubComponent {
        if (!::authenticationSubComponent.isInitialized) {
            authenticationSubComponent = appComponent.authenticationSubcomponentBuilder.build()
        }
        return authenticationSubComponent
    }
}