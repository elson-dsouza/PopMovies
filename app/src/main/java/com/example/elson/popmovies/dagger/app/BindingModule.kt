package com.example.elson.popmovies.dagger.app

import android.content.Context
import com.example.elson.popmovies.MoviesApplication
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BindingModule {

    @Singleton
    @Binds
    abstract fun provideContext(application: MoviesApplication): Context
}