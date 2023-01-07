package com.example.elson.popmovies.ui.movies.grid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.data.repository.MoviesRepository
import com.paginate.Paginate
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "MoviesFragVM"

class MoviesFragmentViewModel : ViewModel(), Paginate.Callbacks {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList: LiveData<List<MovieModel>> = _movieList

    @Inject
    lateinit var moviesRepository: MoviesRepository

    var mode = MovieTypes.POPULAR

    init {
        AppInjector.getMoviesComponent().inject(this)
    }

    private var currentPage = 1
    private var lastPage = 1

    fun refreshMovieList() {
        _isLoading.value = true
        currentPage = 1
        lastPage = 1
        _movieList.value = listOf()
        fetchMoviesForPage(currentPage)
    }

    override fun onLoadMore() {
        _isLoading.value = true
        currentPage++
        fetchMoviesForPage(currentPage)
    }

    private fun fetchMoviesForPage(pageNo: Int) {
        viewModelScope.launch {
            val result = moviesRepository
                    .fetchMoviesAsync(mode.queryPath, pageNo).await()
            if (result is Result.Success) {
                currentPage = result.data.current
                lastPage = result.data.total
                _movieList.value = result.data.result
            } else {
                Log.e(LOG_TAG, "Error fetching movies", (result as Result.Error).exception)
            }
            _isLoading.value = false
        }
    }

    override fun isLoading(): Boolean = isLoading.value ?: false

    override fun hasLoadedAllItems() = currentPage == lastPage

    fun toggleFavouriteState(movie: MovieModel) {
        viewModelScope.launch {
            moviesRepository.toggleFavouriteStateAsync(movie)
        }
    }
}