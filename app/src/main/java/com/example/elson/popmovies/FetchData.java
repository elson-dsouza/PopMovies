package com.example.elson.popmovies;

import com.example.elson.pojo.MovieData;
import com.example.elson.pojo.MovieHeader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Elson on 20-05-2016.
 */
public interface FetchData {

    @GET("/3/movie/{task}")
    Call<MovieHeader> getMovies(@Path("task") String task, @Query("api_key") String key);

    @GET("/3/movie/{id}")
    Call<MovieData> getData(@Path("id") Long id, @Query("api_key") String key);
}
