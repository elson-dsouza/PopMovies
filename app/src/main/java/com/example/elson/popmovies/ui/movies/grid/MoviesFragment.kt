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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.databinding.MoviesFragmentBinding
import com.paginate.Paginate

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

    private lateinit var fragmentViewModel: MoviesFragmentViewModel
    private lateinit var movieGridAdapter: MoviesGridAdapter
    private val activityViewModel: MoviesActivityViewModel by activityViewModels()
    private lateinit var binding: MoviesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val movieLayoutManager = GridLayoutManager(context, NUM_COLS)
        movieGridAdapter = MoviesGridAdapter(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        binding.apply {
            // Initializing the Grid Recycler View
            movieGrid.layoutManager = movieLayoutManager
            movieGrid.adapter = movieGridAdapter
            movieGrid.setItemViewCacheSize(ITEM_CACHE_SIZE)

            // Setup swipe to refresh
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = true
                movieGridAdapter.clear()
                fragmentViewModel.refreshMovieList()
                fragmentViewModel.isLoading.observe(viewLifecycleOwner) {
                    refreshLayout.isRefreshing = it
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mode = arguments?.getString(ARG_MOVIE_TYPE)?.let { MovieTypes.valueOf(it) }
            ?: MovieTypes.POPULAR
        fragmentViewModel = ViewModelProvider(this).get(MoviesFragmentViewModel::class.java)
        fragmentViewModel.mode = mode
        fragmentViewModel.refreshMovieList()

        // Setup pagination
        Paginate.with(binding.movieGrid, fragmentViewModel)
            .addLoadingListItem(true)
            .setLoadingListItemSpanSizeLookup { NUM_COLS }
            .build()

        fragmentViewModel.movieList.observe(viewLifecycleOwner) {
            movieGridAdapter.add(it)
            movieGridAdapter.notifyDataSetChanged()
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
