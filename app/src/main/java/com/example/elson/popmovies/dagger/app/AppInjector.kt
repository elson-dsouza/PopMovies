package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.dagger.authentication.AuthenticationSubComponent

object AppInjector {

    lateinit var appComponent: AppComponent
    private lateinit var authenticationSubComponent: AuthenticationSubComponent

    @Synchronized
    fun getAuthenticationSubComponent(): AuthenticationSubComponent {
        if (!::authenticationSubComponent.isInitialized) {
            authenticationSubComponent = appComponent.authenticationSubComponentFactory.create()
        }
        return authenticationSubComponent
    }
}