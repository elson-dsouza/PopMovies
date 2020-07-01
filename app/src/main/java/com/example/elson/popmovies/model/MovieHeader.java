package com.example.elson.popmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Elson on 21-05-2016.
 */
public class MovieHeader {

    @SerializedName("page") int page;
    @SerializedName("results") List<MovieData> result;
    @SerializedName("total_pages") int total;


    public List<MovieData> getResult(){
        return result;
    }

    public int getCurrent(){
        return page;
    }

    public int getTotal() {
        return total;
    }

}