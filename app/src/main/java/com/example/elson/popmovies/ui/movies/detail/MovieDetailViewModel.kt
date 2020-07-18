package com.example.elson.popmovies.ui.movies.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.MovieData
import com.example.elson.popmovies.data.model.FullMovieData
import com.example.elson.popmovies.data.repository.MoviesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "MovieDetailVM"

class MovieDetailViewModel : ViewModel() {

    private val _fullMovieData = MutableLiveData<FullMovieData>()
    val fullMovieData: LiveData<FullMovieData> = _fullMovieData

    @Inject
    lateinit var moviesRepository: MoviesRepository

    init {
        AppInjector.getMoviesComponent().inject(this)
    }

    fun load(movie: MovieData) {
        viewModelScope.launch {
            val result = moviesRepository.fetchMovieDetailsAsync(movie.id).await()
            if (result is Result.Success) {
                _fullMovieData.value = result.data
            } else {
                Log.e(LOG_TAG, "Unable to fetch movie details",
                        (result as Result.Error).exception)
            }
        }
    }
}