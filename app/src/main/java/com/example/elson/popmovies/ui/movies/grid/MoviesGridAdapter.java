package com.example.elson.popmovies.ui.movies.grid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elson.popmovies.BR;
import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.model.MovieModel;
import com.example.elson.popmovies.databinding.MoviesBinding;

import java.util.ArrayList;
import java.util.List;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesViewHolder> {

    private final List<MovieModel> movieList;
    private final MoviesFragment moviesFragment;

    public MoviesGridAdapter(@NonNull MoviesFragment moviesFragment) {
        this.movieList = new ArrayList<>();
        this.moviesFragment = moviesFragment;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        MoviesBinding binding = DataBindingUtil.inflate(inflater, R.layout.movies, parent,
                false);
        return new MoviesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesViewHolder movieViewHolder, int position) {
        MovieModel movie = movieList.get(position);
        movieViewHolder.setMovieModel(movie);
        moviesFragment.loadMoviePoster(movie.getPoster(), movieViewHolder.getPosterView());
        movieViewHolder.setItemClickListener((view) -> moviesFragment.loadDetails(movie));
        movieViewHolder.setFavoriteClickListener((view) -> moviesFragment.toggleFavouriteState(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void add(@NonNull List<MovieModel> data) {
        movieList.addAll(data);
    }

    public void clear() {
        movieList.clear();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private final MoviesBinding moviesBinding;

        MoviesViewHolder(@NonNull MoviesBinding moviesBinding) {
            super(moviesBinding.getRoot());
            this.moviesBinding = moviesBinding;
        }

        void setMovieModel(@NonNull MovieModel model) {
            moviesBinding.setModel(model);
            moviesBinding.notifyPropertyChanged(BR.model);
        }

        ImageView getPosterView() {
            return moviesBinding.moviePoster;
        }

        void setItemClickListener(@NonNull View.OnClickListener listener) {
            moviesBinding.getRoot().setOnClickListener(listener);
        }

        void setFavoriteClickListener(@NonNull View.OnClickListener listener) {
            moviesBinding.favoriteButton.setOnClickListener(listener);
        }
    }
}
