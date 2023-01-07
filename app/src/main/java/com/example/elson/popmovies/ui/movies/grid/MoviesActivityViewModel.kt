package com.example.elson.popmovies.ui.movies.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elson.popmovies.data.model.MovieModel

class MoviesActivityViewModel: ViewModel() {

    private val _selectedMovie = MutableLiveData<MovieModel>()
    val selectedMovie: LiveData<MovieModel> = _selectedMovie

    fun loadDetails(movie: MovieModel) {
        _selectedMovie.value = movie
    }

    fun clearSelectedMovie() {
        _selectedMovie.value = null
    }

}