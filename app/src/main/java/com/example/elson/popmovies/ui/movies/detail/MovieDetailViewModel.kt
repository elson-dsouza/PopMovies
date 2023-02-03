package com.example.elson.popmovies.ui.movies.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.FullMovieModel
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.data.repository.MoviesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "MovieDetailVM"

class MovieDetailViewModel : ViewModel() {

    private val _fullMovieModel = MutableLiveData<FullMovieModel>()
    val fullMovieModel: LiveData<FullMovieModel> = _fullMovieModel

    private val _movieModel = MutableLiveData<MovieModel>()

    @Inject
    lateinit var moviesRepository: MoviesRepository

    init {
        AppInjector.getMoviesComponent().inject(this)
    }

    fun load(movie: MovieModel) {
        _movieModel.value = movie
        viewModelScope.launch {
            val result = moviesRepository.fetchMovieDetailsAsync(movie.id).await()
            if (result is Result.Success) {
                _fullMovieModel.value = result.data
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
