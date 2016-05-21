package com.example.elson.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.elson.pojo.Result;

import java.util.List;

/**
 * Created by Elson on 20-05-2016.
 */
public class GridAdapter extends ArrayAdapter<Result> {
    public GridAdapter(Context applicationContext, List<Result> movieList) {
        super(applicationContext,0, movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String poster=getItem(position).getPoster();
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.movies,parent,false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.movieView);
        Glide.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+poster).into(iconView);
        return convertView;
    }
}

