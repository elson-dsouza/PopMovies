package com.example.elson.popmovies.ui.movies.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.elson.popmovies.dagger.app.AppInjector
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

private val DEFAULT_MODE = MovieTypes.POPULAR

class MoviesFragmentViewModel : ViewModel() {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private val _currentMode = MutableStateFlow(DEFAULT_MODE)
    val currentMode: StateFlow<MovieTypes> = _currentMode

    val pagingDataFlow: Flow<PagingData<MovieModel>>

    /**
     * Processor of side effects from the UI which in turn feedback into [currentMode]
     */
    val accept: (MovieTypes) -> Unit

    init {
        AppInjector.getMoviesComponent().inject(this)

        @OptIn(ExperimentalCoroutinesApi::class)
        pagingDataFlow = currentMode
            .flatMapLatest { moviesRepository.fetchMoviesList(it.queryPath) }
            .cachedIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { _currentMode.emit(action) }
        }
    }

    fun toggleFavouriteState(movie: MovieModel) {
        viewModelScope.launch {
            moviesRepository.toggleFavouriteStateAsync(movie)
        }
    }
}
