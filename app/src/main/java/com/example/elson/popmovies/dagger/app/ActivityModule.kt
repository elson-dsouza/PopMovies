package com.example.elson.popmovies.dagger.app

import com.example.elson.popmovies.ui.movies.grid.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesActivity(): MoviesActivity
}