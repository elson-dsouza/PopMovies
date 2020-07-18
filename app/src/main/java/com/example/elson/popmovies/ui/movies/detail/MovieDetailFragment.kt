package com.example.elson.popmovies.ui.movies.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.model.MovieData
import com.example.elson.popmovies.databinding.MovieDetailFragmentBinding
import kotlinx.android.synthetic.main.movie_detail_content.moviePoster
import kotlinx.android.synthetic.main.movie_detail_fragment.movieBackdrop

internal const  val ARG_MOVIE_DATA = "MOVIE_DATA"
private const val LOG_TAG = "MovieDetailFrag"

class MovieDetailFragment : Fragment() {

    private lateinit var movieDetailFragmentBinding: MovieDetailFragmentBinding

    companion object {
        @JvmStatic
        fun newInstance(movie: MovieData) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MOVIE_DATA, movie)
            }
        }
    }

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        movieDetailFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.movie_detail_fragment, container, false)
        return movieDetailFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        if (savedInstanceState == null) {
            val movie = arguments?.getParcelable<MovieData>(ARG_MOVIE_DATA)
                    ?: error("Movie not passed in")
            viewModel.load(movie)
        }
        viewModel.fullMovieData.observe(viewLifecycleOwner, Observer { movieDetails ->
            movieDetailFragmentBinding.model = movieDetails
            movieDetailFragmentBinding.notifyPropertyChanged(BR.model)
            loadMovieImage(movieDetails.backdrop, movieBackdrop)
            loadMovieImage(movieDetails.poster, moviePoster)
        })
    }

    private fun loadMovieImage(imagePath: String?, imageView: ImageView) {
        if (imagePath.isNullOrBlank()) {
            Log.e(LOG_TAG, "Image path for movie is null or blank")
            return
        }
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185/$imagePath")
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
    }
}