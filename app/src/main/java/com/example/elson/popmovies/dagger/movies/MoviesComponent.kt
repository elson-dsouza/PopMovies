package com.example.elson.popmovies.dagger.movies

import com.example.elson.popmovies.ui.movies.detail.MovieDetailViewModel
import com.example.elson.popmovies.ui.movies.grid.MoviesActivityViewModel
import dagger.Subcomponent

@MoviesScope
@Subcomponent(
    modules = [
        MoviesModule::class
    ]
)
interface MoviesComponent {

    fun inject(viewModel: MovieDetailViewModel)
    fun inject(moviesActivityViewModel: MoviesActivityViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesComponent
    }
}
