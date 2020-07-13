package com.example.elson.popmovies.ui.movies;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elson.popmovies.BR;
import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.model.MovieData;
import com.example.elson.popmovies.data.model.MovieFullData;
import com.example.elson.popmovies.databinding.MoviesBinding;
import com.example.elson.popmovies.network.FetchData;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MoviesViewHolder> {

    private final List<MovieData> movieList;
    private final MoviesFragment moviesFragment;

    public MoviesGridAdapter(@NonNull List<MovieData> movieList,
                             @NonNull MoviesFragment moviesFragment) {
        this.movieList = new ArrayList<>(movieList);
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
//            moviesFragment.toggleFavouriteState(movie);
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

        public MoviesViewHolder(@NonNull MoviesBinding moviesBinding) {
            super(moviesBinding.getRoot());
            this.moviesBinding = moviesBinding;
        }

        public void setMovieModel(@NonNull MovieData model) {
            moviesBinding.setModel(model);
            moviesBinding.notifyPropertyChanged(BR.model);
        }

        public ImageView getPosterView() {
            return moviesBinding.moviePoster;
        }

    }
}
