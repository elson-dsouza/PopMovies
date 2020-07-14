package com.example.elson.popmovies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.MovieData
import com.example.elson.popmovies.data.repository.MoviesRepository
import com.paginate.Paginate
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesFragmentViewModel : ViewModel(), Paginate.Callbacks {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _movieList = MutableLiveData<List<MovieData>>()
    val movieList: LiveData<List<MovieData>> = _movieList

    private var category: String = "popular"

    @Inject
    lateinit var moviesRepository: MoviesRepository

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
                    .fetchMoviesAsync(category, pageNo).await()
            if (result is Result.Success) {
                currentPage = result.data.current
                lastPage = result.data.total
                _movieList.value = result.data.result
            } else {

            }
            _isLoading.value = false
        }
    }

    override fun isLoading(): Boolean = isLoading.value ?: false

    override fun hasLoadedAllItems() = currentPage == lastPage
}