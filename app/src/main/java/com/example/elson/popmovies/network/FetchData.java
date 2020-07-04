package com.example.elson.popmovies.network;

import androidx.annotation.NonNull;

import com.example.elson.popmovies.data.model.MovieFullData;
import com.example.elson.popmovies.data.model.MovieHeader;
import com.example.elson.popmovies.data.model.ReviewsHeader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Elson on 20-05-2016.
 */
public interface FetchData {

    @NonNull
    @GET("/3/movie/{task}")
    Call<MovieHeader> getMovies(@Path("task") String task, @Query("api_key") String key, @Query("page") String pg);

    @NonNull
    @GET("/3/movie/{id}")
    Call<MovieFullData> getData(@Path("id") String id, @Query("api_key") String key);

//    @GET("/3/movie/{id}/videos")
//    Call<VideoResponse> getVideos(@Path("id") String id, @Query("api_key") String key);

    @NonNull
    @GET("/3/movie/{id}/reviews")
    Call<ReviewsHeader> getReviews(@Path("id") String id, @Query("api_key") String key, @Query("page") String pg);

}
