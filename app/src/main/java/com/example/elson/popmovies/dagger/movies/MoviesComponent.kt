package com.example.elson.popmovies.dagger.movies

import com.example.elson.popmovies.ui.movies.MoviesFragmentViewModel
import dagger.Subcomponent

@MoviesScope
@Subcomponent(modules = [
    MoviesModule::class
])
interface MoviesComponent {

    fun inject(viewModel: MoviesFragmentViewModel)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesComponent
    }

}