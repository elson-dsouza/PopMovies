package com.example.elson.popmovies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.model.MovieData

class MoviesFragment : Fragment() {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movies_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun loadMoviePoster(moviePoster: String, posterView: ImageView) {
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185/$moviePoster")
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(posterView)
    }

    fun toggleFavouriteState(movie: MovieData) {

    }

}