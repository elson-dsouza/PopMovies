package com.example.elson.popmovies.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Elson on 10-10-2016.
 */
public class MovieFullData extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    int id;
    @SerializedName("title")
    String title;
    @SerializedName("runtime")
    int duration;
    @SerializedName("release_date")
    String release;
    @SerializedName("vote_average")
    Double rating;
    @SerializedName("overview")
    String description;
    @SerializedName("poster_path")
    String poster;
    @SerializedName("adult")
    Boolean isAdult;
    @SerializedName("backdrop_path")
    String backdrop;

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return Integer.toString(duration) + "min";
    }

    public String getRating() {
        return Double.toString(rating) + "/10";
    }

    public String getDescription() {
        return description;
    }

    public String getPoster() {
        return poster;
    }

    public int getId() {
        return id;
    }

    public String getRelease() {
        return release;
    }

    public Boolean getIsAdult() {
        return isAdult;
    }

    public String getBackdrop() {
        return backdrop;
    }

}
