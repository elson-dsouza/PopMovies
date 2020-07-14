package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.MoviesApplication
import com.example.elson.popmovies.dagger.authentication.AuthenticationComponent
import com.example.elson.popmovies.dagger.movies.MoviesComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    BindingModule::class,
    AppModule::class,
    ActivityModule::class
])
interface AppComponent: AndroidInjector<MoviesApplication> {

    override fun inject(application: MoviesApplication)
    val authenticationComponentFactory: AuthenticationComponent.Factory
    val moviesComponentFactory: MoviesComponent.Factory

    @Component.Factory
    interface Factory: AndroidInjector.Factory<MoviesApplication> {
        override fun create(@BindsInstance instance: MoviesApplication): AppComponent
    }
}