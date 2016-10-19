package com.example.elson.popmovies.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Elson on 19-10-2016.
 */
public class ReviewsHeader {
    @SerializedName("page")
    int page;
    @SerializedName("results")
    List<Reviews> result;
    @SerializedName("total_pages")
    int total;

    public int getPage() {
        return page;
    }

    public List<Reviews> getResult() {
        return result;
    }

    public int getTotal() {
        return total;
    }
}
