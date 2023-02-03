package com.example.elson.popmovies.ui.movies.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.SecurePrefs
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.data.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesActivityViewModel : ViewModel() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    @Inject
    lateinit var prefs: SecurePrefs

    private val _currentMode: MutableStateFlow<MovieTypes>
    val currentMode: StateFlow<MovieTypes>

    private val _selectedMovie = MutableLiveData<MovieModel?>()
    val selectedMovie: LiveData<MovieModel?> = _selectedMovie

    val pagingDataFlow: Flow<PagingData<MovieModel>>

    init {
        AppInjector.getMoviesComponent().inject(this)

        _currentMode = MutableStateFlow(prefs.getMoviesMode())
        currentMode = _currentMode

        @OptIn(ExperimentalCoroutinesApi::class)
        pagingDataFlow = currentMode
            .flatMapLatest { moviesRepository.fetchMoviesList(it.queryPath) }
            .cachedIn(viewModelScope)
    }

    fun updateMovieMode(movieType: MovieTypes) {
        prefs.setMoviesMode(movieType)
        _currentMode.tryEmit(movieType)
    }

    fun toggleFavouriteState(movie: MovieModel) {
        viewModelScope.launch {
            moviesRepository.toggleFavouriteStateAsync(movie)
        }
    }

    fun loadDetails(movie: MovieModel) {
        _selectedMovie.value = movie
    }

    fun clearSelectedMovie() {
        _selectedMovie.value = null
    }
}
