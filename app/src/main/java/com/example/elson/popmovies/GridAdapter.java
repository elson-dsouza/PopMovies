package com.example.elson.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elson.popmovies.pojo.MovieData;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter {

    private List<MovieData> movieList;
    private Context context;

    public GridAdapter(List<MovieData> movieList) {
        this.movieList=movieList;
    }

    public GridAdapter() {

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = View.inflate(context, R.layout.movies, null);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position==movieList.size())
            return;
        final MovieData movie = movieList.get(position);
        final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        movieViewHolder.movieName.setText(movie.getTitle());
        movieViewHolder.movieRating.setText("\t"+movie.getRating()+"\t");
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/"+movie.getPoster()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(movieViewHolder.moviePoster);
//        if(LocalStoreUtil.hasInFavorites(context, movies.getId())) {
//            movieViewHolder.mFavoriteButton.setSelected(true);
//            movies.setFavorite(true);
//        } else {
//            movieViewHolder.mFavoriteButton.setSelected(false);
//            movies.setFavorite(false);
//        }

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mCallbacks!=null) {
//                    mCallbacks.onMovieClick(movies);
//                }
            }
        });

        movieViewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                movieViewHolder.favoriteButton.setImageResource(R.drawable.ic_action_favorite_small);
//                if(mCallbacks!=null) {
//                    movieViewHolder.mFavoriteButton.setSelected(!movies.isFavorite());
//                    mCallbacks.onFavoriteClick(movies);
//                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (movieList!=null)?movieList.size()+1:0;
    }

    public void add(List<MovieData> data){ movieList.addAll(data); }

    public void clear(){movieList.clear();}

}

