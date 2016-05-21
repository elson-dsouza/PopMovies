package com.example.elson.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elson on 21-05-2016.
 */
public class MovieData {

    @SerializedName("original_title") String title;
    @SerializedName("runtime") int duration;
    @SerializedName("release_date")String release;
    @SerializedName("vote_average") Double rating;
    @SerializedName("overview") String plot;
    @SerializedName("poster_path") String poster;

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return Integer.toString(duration)+"min";
    }

    public String getYear() {
        return release.split("-")[0];
    }

    public String getRating() {
        return Double.toString(rating)+"/10";
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }
}
