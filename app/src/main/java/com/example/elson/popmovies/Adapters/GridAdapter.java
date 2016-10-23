package com.example.elson.popmovies.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.FetchData;
import com.example.elson.popmovies.MovieDetail;
import com.example.elson.popmovies.MovieDetailFragment;
import com.example.elson.popmovies.R;
import com.example.elson.popmovies.pojo.MovieData;
import com.example.elson.popmovies.pojo.MovieFullData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GridAdapter extends RecyclerView.Adapter {

    private Realm realm;
    private List<Parcelable> movieList;
    private Context context;
    private boolean isTwoPane;
    private FragmentManager fm;
    private MovieData movie;

    public GridAdapter(List<Parcelable> movieList, boolean isTwoPane, FragmentManager fm, Realm realm) {
        this.movieList = new ArrayList<>();
        add(movieList);
        this.isTwoPane = isTwoPane;
        this.fm = fm;
        this.realm = realm;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(context, R.layout.movies, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (movieList.get(position) instanceof MovieData)
            movie = (MovieData) movieList.get(position);
        else
            movie = new MovieData((MovieFullData) movieList.get(position));
        final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        movieViewHolder.movieName.setText(movie.getTitle());
        movieViewHolder.movieRating.setText("\t" + movie.getRating() + "\t");
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(movieViewHolder.moviePoster);
        if (realm.where(MovieFullData.class).equalTo("id", movie.getId()).findFirst() != null)
            movieViewHolder.favoriteButton.setImageResource(R.drawable.ic_action_favorite_small);
        else
            movieViewHolder.favoriteButton.setImageResource(R.drawable.ic_action_favorite_outline_small);


        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTwoPane) {
                    Intent i = new Intent(v.getContext(), MovieDetail.class);
                    i.putExtra("data", movieList.get(holder.getAdapterPosition()));
                    v.getContext().startActivity(i);

                } else {
                    MovieDetailFragment detailFragment = new MovieDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", movieList.get(holder.getAdapterPosition()));
                    detailFragment.setArguments(bundle);
                    fm.beginTransaction()
                            .replace(R.id.container, detailFragment)
                            .commit();
                }
            }
        });

        movieViewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<MovieFullData> temp = realm.where(MovieFullData.class).equalTo("id", movie.getId()).findAll();
                if (temp.size() == 0) {
                    movieViewHolder.favoriteButton.setImageResource(R.drawable.ic_action_favorite_small);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.themoviedb.org")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    FetchData fetch = retrofit.create(FetchData.class);
                    Call<MovieFullData> call = fetch.getData(Integer.toString(movie.getId()), BuildConfig.API_KEY);
                    call.enqueue(new Callback<MovieFullData>() {
                        @Override
                        public void onResponse(Call<MovieFullData> call, Response<MovieFullData> response) {
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
                    movieViewHolder.favoriteButton.setImageResource(R.drawable.ic_action_favorite_outline_small);
                    realm.beginTransaction();
                    temp.deleteAllFromRealm();
                    realm.commitTransaction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movieList != null) ? movieList.size() : 0;
    }

    public void add(List<Parcelable> data) {
        if (data != null)
            movieList.addAll(data);
    }

    public void clear() {
        movieList.clear();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView movieName;
        private TextView movieRating;
        private ImageView moviePoster;
        private ImageButton favoriteButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movieName);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            favoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
            movieRating = (TextView) itemView.findViewById(R.id.movieRating);
        }
    }

}

