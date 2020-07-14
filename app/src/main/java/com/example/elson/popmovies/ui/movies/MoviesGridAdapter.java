package com.example.elson.popmovies.ui.movies;

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
import com.example.elson.popmovies.data.model.MovieData;
import com.example.elson.popmovies.databinding.MoviesBinding;

import java.util.ArrayList;
import java.util.List;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesViewHolder> {

    private final List<MovieData> movieList;
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
        MovieData movie = movieList.get(position);
        movieViewHolder.setMovieModel(movie);
        moviesFragment.loadMoviePoster(movie.getPoster(), movieViewHolder.getPosterView());
        movieViewHolder.setItemClickListener((view) -> moviesFragment.loadDetails(movie));
        movieViewHolder.setFavoriteClickListener((view) -> moviesFragment.toggleFavouriteState(movie));

//        movieViewHolder.getRoot().setOnClickListener(v -> {
//            if (!isTwoPane) {
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
//        });
//
//        movieViewHolder.favoriteButton.setOnClickListener(v -> {
//            RealmResults<MovieFullData> temp = realm.where(MovieFullData.class)
//                    .equalTo("id", movie.getId()).findAll();
//            if (temp.size() == 0) {
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://api.themoviedb.org")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                FetchData fetch = retrofit.create(FetchData.class);
//                Call<MovieFullData> call = fetch.getData(Integer.toString(movie.getId()),
//                        BuildConfig.TMDB_V3_API_TOKEN);
//                call.enqueue(new Callback<MovieFullData>() {
//                    @Override
//                    public void onResponse(Call<MovieFullData> call, @NonNull Response<MovieFullData> response) {
//                        MovieFullData data = response.body();
//                        realm.beginTransaction();
//                        realm.copyToRealmOrUpdate(data);
//                        realm.commitTransaction();
//                    }
//
//                    @Override
//                    public void onFailure(Call<MovieFullData> call, Throwable t) {
//
//                    }
//                });
//            } else {
//                realm.beginTransaction();
//                temp.deleteAllFromRealm();
//                realm.commitTransaction();
//            }
//            ;
//        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void add(@NonNull List<MovieData> data) {
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

        void setMovieModel(@NonNull MovieData model) {
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
