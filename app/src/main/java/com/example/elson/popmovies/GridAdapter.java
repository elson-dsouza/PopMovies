package com.example.elson.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.elson.popmovies.pojo.MovieData;

import java.util.List;

public class GridAdapter extends ArrayAdapter<MovieData> {
    public GridAdapter(Context applicationContext, List<MovieData> movieList) {
        super(applicationContext,0, movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String poster=getItem(position).getPoster();
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movies,parent,false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.movieView);
        Glide.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+poster).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iconView);
        return convertView;
    }
}

