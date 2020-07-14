package com.example.elson.popmovies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.model.MovieData
import com.paginate.Paginate
import kotlinx.android.synthetic.main.movies_fragment.movieGrid
import kotlinx.android.synthetic.main.movies_fragment.view.refreshLayout
import kotlinx.android.synthetic.main.movies_fragment.view.movieGrid

private const val NUM_COLS = 2
private const val ITEM_CACHE_SIZE = 32

class MoviesFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(query: String) = MoviesFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    private lateinit var fragmentViewModel: MoviesFragmentViewModel
    private lateinit var movieGridAdapter: MoviesGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val movieLayoutManager = GridLayoutManager(context, NUM_COLS)
        movieGridAdapter = MoviesGridAdapter(this)

        return inflater.inflate(R.layout.movies_fragment, container, false).apply {
            //Initializing the Grid Recycler View
            movieGrid.layoutManager = movieLayoutManager
            movieGrid.adapter = movieGridAdapter
            movieGrid.setItemViewCacheSize(ITEM_CACHE_SIZE)

            //Setup swipe to refresh
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = true
                movieGridAdapter.clear()
                fragmentViewModel.refreshMovieList()
                fragmentViewModel.isLoading.observe(viewLifecycleOwner, Observer {
                    refreshLayout.isRefreshing = it
                })
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProvider(this).get(MoviesFragmentViewModel::class.java)
        fragmentViewModel.refreshMovieList()

        //Setup pagination
        Paginate.with(movieGrid, fragmentViewModel)
                .addLoadingListItem(true)
                .setLoadingListItemSpanSizeLookup { NUM_COLS }
                .build()

        fragmentViewModel.movieList.observe(viewLifecycleOwner, Observer {
            movieGridAdapter.add(it)
            movieGridAdapter.notifyDataSetChanged()
        })
    }

    fun loadMoviePoster(moviePoster: String, posterView: ImageView) {
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185/$moviePoster")
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(posterView)
    }

    fun toggleFavouriteState(movie: MovieData) {

    }

    fun loadDetails(movie: MovieData) {

    }
}