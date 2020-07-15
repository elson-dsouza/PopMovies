package com.example.elson.popmovies.ui.movies

import android.os.Bundle
import android.util.Log
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
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieData
import com.paginate.Paginate
import kotlinx.android.synthetic.main.movies_fragment.movieGrid
import kotlinx.android.synthetic.main.movies_fragment.view.refreshLayout
import kotlinx.android.synthetic.main.movies_fragment.view.movieGrid

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
        val mode = arguments?.getString(ARG_MOVIE_TYPE)?.let { MovieTypes.valueOf(it) }
                ?: MovieTypes.POPULAR
        fragmentViewModel = ViewModelProvider(this).get(MoviesFragmentViewModel::class.java)
        fragmentViewModel.mode = mode
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

    fun toggleFavouriteState(movie: MovieData) {
        fragmentViewModel.toggleFavouriteState(movie)
    }

    fun loadDetails(movie: MovieData) {
//        if (!isTwoPane) {
//                Intent i = new Intent(v.getContext(), MovieDetail.class);
//                i.putExtra("data", movieList.get(holder.getAdapterPosition()));
//                v.getContext().startActivity(i);
//
//            } else {
//                MovieDetailFragment detailFragment = new MovieDetailFragment();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("data", movieList.get(holder.getAdapterPosition()));
//                detailFragment.setArguments(bundle);
//                fm.beginTransaction()
//                        .replace(R.id.container, detailFragment)
//                        .commit();
//            }
    }
}