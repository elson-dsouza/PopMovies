package com.example.elson.popmovies.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elson on 19-10-2016.
 */
public class ReviewsHeader {
    @SerializedName("page")
    int page;
//    @SerializedName("results")
//    List<Reviews> result;
    @SerializedName("total_pages")
    int total;

    public int getPage() {
        return page;
    }

//    public List<Reviews> getResult() {
//        return result;
//    }

    public int getTotal() {
        return total;
    }
}
