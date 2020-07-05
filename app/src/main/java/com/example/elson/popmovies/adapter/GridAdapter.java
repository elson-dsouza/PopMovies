package com.example.elson.popmovies.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.network.FetchData;
import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.model.MovieData;
import com.example.elson.popmovies.data.model.MovieFullData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MovieViewHolder> {

    private final Realm realm;
    @NonNull
    private final List<Parcelable> movieList;
    private Context context;
    private final boolean isTwoPane;
    private final FragmentManager fm;
    private MovieData movie;

    public GridAdapter(List<Parcelable> movieList, boolean isTwoPane, FragmentManager fm, Realm realm) {
        this.movieList = new ArrayList<>();
        add(movieList);
        this.isTwoPane = isTwoPane;
        this.fm = fm;
        this.realm = realm;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(context, R.layout.movies, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, int position) {

        if (movieList.get(position) instanceof MovieData)
            movie = (MovieData) movieList.get(position);
        else
            movie = new MovieData((MovieFullData) movieList.get(position));

        movieViewHolder.movieName.setText(movie.getTitle());
        movieViewHolder.movieRating.setText(String.format("\t%s\t", movie.getRating()));
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(movieViewHolder.moviePoster);
        movieViewHolder.favoriteButton.setChecked(realm.where(MovieFullData.class)
                .equalTo("id", movie.getId()).findFirst() != null);


        movieViewHolder.itemView.setOnClickListener(v -> {
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
        });

        movieViewHolder.favoriteButton.setOnClickListener(v -> {
            movieViewHolder.favoriteButton.toggle();
            RealmResults<MovieFullData> temp = realm.where(MovieFullData.class)
                    .equalTo("id", movie.getId()).findAll();
            if (temp.size() == 0) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                FetchData fetch = retrofit.create(FetchData.class);
                Call<MovieFullData> call = fetch.getData(Integer.toString(movie.getId()),
                        BuildConfig.TMDB_V3_API_TOKEN);
                call.enqueue(new Callback<MovieFullData>() {
                    @Override
                    public void onResponse(Call<MovieFullData> call, @NonNull Response<MovieFullData> response) {
                        MovieFullData data = response.body();
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(data);
                        realm.commitTransaction();
                    }

                    @Override
                    public void onFailure(Call<MovieFullData> call, Throwable t) {

                    }
                });
            } else {
                realm.beginTransaction();
                temp.deleteAllFromRealm();
                realm.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void add(@Nullable List<Parcelable> data) {
        if (data != null)
            movieList.addAll(data);
    }

    public void clear() {
        movieList.clear();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final TextView movieName;
        @NonNull
        private final TextView movieRating;
        @NonNull
        private final ImageView moviePoster;
        private final CheckBox favoriteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movieName);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            movieRating = itemView.findViewById(R.id.movieRating);
        }
    }

}

