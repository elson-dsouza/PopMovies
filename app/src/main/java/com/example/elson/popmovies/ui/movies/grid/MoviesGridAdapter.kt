package com.example.elson.popmovies.ui.movies.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.elson.popmovies.BR
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.databinding.MoviesBinding
import com.example.elson.popmovies.ui.movies.grid.MoviesGridAdapter.MoviesViewHolder

class MoviesGridAdapter(
    moviesFragment: MoviesFragment
) : PagingDataAdapter<MovieModel, MoviesViewHolder>(MOVIE_DIFF_CALLBACK) {
    private val moviesFragment: MoviesFragment

    init {
        this.moviesFragment = moviesFragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = MoviesBinding.inflate(inflater, parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(movieViewHolder: MoviesViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        movieViewHolder.setMovieModel(movie)
        moviesFragment.loadMoviePoster(movie.poster, movieViewHolder.posterView)
        movieViewHolder.setItemClickListener { moviesFragment.loadDetails(movie) }
        movieViewHolder.setFavoriteClickListener {
            moviesFragment.toggleFavouriteState(movie)
        }
    }

    class MoviesViewHolder(
        private val moviesBinding: MoviesBinding
    ) : RecyclerView.ViewHolder(moviesBinding.root) {

        fun setMovieModel(model: MovieModel) {
            moviesBinding.model = model
            moviesBinding.notifyPropertyChanged(BR.model)
        }

        val posterView: ImageView
            get() = moviesBinding.moviePoster

        fun setItemClickListener(listener: View.OnClickListener) {
            moviesBinding.root.setOnClickListener(listener)
        }

        fun setFavoriteClickListener(listener: View.OnClickListener) {
            moviesBinding.favoriteButton.setOnClickListener(listener)
        }
    }

    companion object {
        private val MOVIE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieModel>() {
            override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
                oldItem == newItem
        }
    }
}
