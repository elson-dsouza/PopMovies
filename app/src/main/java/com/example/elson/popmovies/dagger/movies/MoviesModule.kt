package com.example.elson.popmovies.dagger.movies

import com.example.elson.popmovies.data.repository.MoviesRepository
import com.example.elson.popmovies.network.Movies
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MoviesModule {

    @MoviesScope
    @Provides
    fun provideMoviesRequest(retrofit: Retrofit): Movies {
        return retrofit.create(Movies::class.java)
    }

    @MoviesScope
    @Provides
    fun provideMoviesRepository(movies: Movies): MoviesRepository {
        return MoviesRepository(movies)
    }

}