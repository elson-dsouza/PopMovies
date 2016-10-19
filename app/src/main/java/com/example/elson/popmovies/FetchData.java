package com.example.elson.popmovies;

import com.example.elson.popmovies.pojo.MovieFullData;
import com.example.elson.popmovies.pojo.MovieHeader;
import com.example.elson.popmovies.pojo.ReviewsHeader;
import com.example.elson.popmovies.pojo.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Elson on 20-05-2016.
 */
public interface FetchData {

    @GET("/3/movie/{task}")
    Call<MovieHeader> getMovies(@Path("task") String task, @Query("api_key") String key, @Query("page") String pg);

    @GET("/3/movie/{id}")
    Call<MovieFullData> getData(@Path("id") Long id, @Query("api_key") String key);

    @GET("/3/movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") String id, @Query("api_key") String key);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsHeader> getReviews(@Path("id") String id, @Query("api_key") String key, @Query("page") String pg);

}
