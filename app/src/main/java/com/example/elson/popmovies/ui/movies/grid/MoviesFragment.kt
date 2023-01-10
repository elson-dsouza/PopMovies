package com.example.elson.popmovies.ui.movies.grid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.databinding.MoviesFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val NUM_COLS = 2
private const val ITEM_CACHE_SIZE = 32

private const val ARG_MOVIE_TYPE = "MOVIE_TYPE"
private const val LOG_TAG = "MoviesFrag"

class MoviesFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(mode: MovieTypes) = MoviesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_MOVIE_TYPE, mode.name)
            }
        }
    }

    private val activityViewModel: MoviesActivityViewModel by activityViewModels()
    private val fragmentViewModel: MoviesFragmentViewModel by viewModels()
    private lateinit var headerAdapter: MoviesLoadStateAdapter
    private lateinit var movieGridAdapter: MoviesGridAdapter
    private lateinit var binding: MoviesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieGridAdapter = MoviesGridAdapter(this)
        headerAdapter = MoviesLoadStateAdapter()
        val footerAdapter = MoviesLoadStateAdapter()
        val movieLayoutManager = GridLayoutManager(context, NUM_COLS)
        movieLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when {
                position == 0 && headerAdapter.itemCount > 0 -> NUM_COLS
                position == movieGridAdapter.itemCount && footerAdapter.itemCount > 0 -> NUM_COLS
                else -> 1
            }
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        binding.apply {
            // Initializing the Grid Recycler View
            movieGrid.layoutManager = movieLayoutManager
            movieGrid.adapter = movieGridAdapter.withLoadStateHeaderAndFooter(headerAdapter, footerAdapter)
            movieGrid.setItemViewCacheSize(ITEM_CACHE_SIZE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mode = arguments?.getString(ARG_MOVIE_TYPE)?.let { MovieTypes.valueOf(it) }
            ?: MovieTypes.POPULAR
        fragmentViewModel.accept(mode)

        // Collect from the PagingData Flow in the ViewModel, and submit it to the
        // PagingDataAdapter.
        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                fragmentViewModel.pagingDataFlow.collectLatest {
                    movieGridAdapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            movieGridAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                headerAdapter.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && headerAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val errorState = loadState.source.refresh as? LoadState.Error
                    ?: loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.refresh as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(binding.root, "${it.error.message}", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    fun loadMoviePoster(moviePoster: String?, posterView: ImageView) {
        if (moviePoster.isNullOrBlank()) {
            Log.e(LOG_TAG, "Poster for movie is null or blank")
            return
        }
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w185/$moviePoster")
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(posterView)
    }

    fun toggleFavouriteState(movie: MovieModel) {
        fragmentViewModel.toggleFavouriteState(movie)
    }

    fun loadDetails(movie: MovieModel) {
        activityViewModel.loadDetails(movie)
    }
}
