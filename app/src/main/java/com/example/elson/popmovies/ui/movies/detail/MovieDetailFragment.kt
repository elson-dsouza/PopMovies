package com.example.elson.popmovies.ui.movies.detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.model.MovieData
import com.example.elson.popmovies.data.model.Video
import com.example.elson.popmovies.databinding.MovieDetailFragmentBinding
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.movie_detail_content.favoriteButton
import kotlinx.android.synthetic.main.movie_detail_content.genresContainer
import kotlinx.android.synthetic.main.movie_detail_content.moviePoster
import kotlinx.android.synthetic.main.movie_detail_content.videosContainer
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
            loadGenres(movieDetails.genres)
            loadVideos(movieDetails.videos)
        })
        favoriteButton.setOnClickListener {
            viewModel.toggleFavouriteState()
        }
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

    private fun loadGenres(genres: List<String>) {
        genres.forEach { genre ->
            val chip = Chip(context)
            chip.text = genre
            genresContainer.addView(chip)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadVideos(videos: List<Video>) {
        val activity = activity ?: return
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        videos.forEach { video ->
            val view = WebView(context).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    isForceDarkAllowed = true
                }
                setBackgroundColor(Color.TRANSPARENT)
                webChromeClient = MovieDetailWebClient(activity)
                layoutParams = ViewGroup.LayoutParams(displayMetrics.widthPixels, 1024)
                settings.javaScriptEnabled = true
                loadUrl(video.url)
            }
            videosContainer.addView(view)
        }
    }
}