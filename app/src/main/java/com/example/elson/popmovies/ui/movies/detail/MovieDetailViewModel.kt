package com.example.elson.popmovies.ui.movies.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.MovieDetailsModel
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.data.repository.MoviesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "MovieDetailVM"

class MovieDetailViewModel : ViewModel() {

    private val _movieDetailsModel = MutableLiveData<MovieDetailsModel>()
    val movieDetailsModel: LiveData<MovieDetailsModel> = _movieDetailsModel

    private val _movieModel = MutableLiveData<MovieModel>()

    @Inject
    lateinit var moviesRepository: MoviesRepository

    init {
        AppInjector.getMoviesComponent().inject(this)
    }

    fun load(movie: MovieModel) {
        _movieModel.value = movie
        viewModelScope.launch {
            val result = moviesRepository.fetchMovieDetailsAsync(movie.id)
            if (result is Result.Success) {
                _movieDetailsModel.value = result.data
            } else {
                Log.e(
                    LOG_TAG,
                    "Unable to fetch movie details",
                    (result as Result.Error).exception
                )
            }
        }
    }

    fun toggleFavouriteState() {
        viewModelScope.launch {
            _movieModel.value?.let { moviesRepository.toggleFavouriteStateAsync(it) }
        }
    }
}
