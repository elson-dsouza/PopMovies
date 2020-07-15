package com.example.elson.popmovies.ui.movies.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elson.popmovies.data.model.MovieData

class MoviesActivityViewModel: ViewModel() {

    private val _selectedMovie = MutableLiveData<MovieData>()
    val selectedMovie: LiveData<MovieData> = _selectedMovie

    fun loadDetails(movie: MovieData) {
        _selectedMovie.value = movie
    }

    fun clearSelectedMovie() {
        _selectedMovie.value = null
    }

}