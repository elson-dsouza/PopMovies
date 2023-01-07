package com.example.elson.popmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Elson on 21-05-2016.
 */
public class MovieListResult {

    @SerializedName("page") int page;
    @SerializedName("results") List<MovieModel> result;
    @SerializedName("total_pages") int total;

    public List<MovieModel> getResult(){
        return result;
    }

    public int getCurrent(){
        return page;
    }

    public int getTotal() {
        return total;
    }

}