package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.dagger.authentication.AuthenticationComponent
import com.example.elson.popmovies.dagger.movies.MoviesComponent

object AppInjector {

    lateinit var appComponent: AppComponent
    private lateinit var authenticationComponent: AuthenticationComponent
    private lateinit var moviesComponent: MoviesComponent

    @Synchronized
    fun getAuthenticationComponent(): AuthenticationComponent {
        if (!::authenticationComponent.isInitialized) {
            authenticationComponent = appComponent.authenticationComponentFactory.create()
        }
        return authenticationComponent
    }

    fun getMoviesComponent(): MoviesComponent {
        if (!::moviesComponent.isInitialized) {
            moviesComponent = appComponent.moviesComponentFactory.create()
        }
        return moviesComponent
    }
}