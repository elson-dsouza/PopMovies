package com.example.elson.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elson.popmovies.pojo.MovieData;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter {

    private List<MovieData> movieList;
    private Context context;


    public GridAdapter(List<MovieData> movieList) {
        this.movieList=movieList;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView movieName;
        private ImageView moviePoster;
        private ImageButton favoriteButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.movieName);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            favoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();

        View view=View.inflate(context,R.layout.movies,null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (movieList!=null)?movieList.size():0;
    }

}

