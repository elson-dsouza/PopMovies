package com.example.elson.popmovies.network;

import androidx.annotation.NonNull;

import com.example.elson.popmovies.data.model.FullMovieModel;
import com.example.elson.popmovies.data.entity.network.MovieListNetworkEntity;
import com.example.elson.popmovies.data.model.ReviewsHeader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Elson on 20-05-2016.
 */
public interface Movies {

    @NonNull
    @GET("/3/movie/{category}")
    Call<MovieListNetworkEntity> getMovies(@Path("category") String category, @Query("api_key") String key,
                                           @Query("page") int pg);

    @NonNull
    @GET("/3/movie/{id}")
    Call<FullMovieModel> getData(@Path("id") long id, @Query("api_key") String key,
                                 @Query("append_to_response") String responseAdditions);

    @NonNull
    @GET("/3/movie/{id}/reviews")
    Call<ReviewsHeader> getReviews(@Path("id") String id, @Query("api_key") String key,
                                   @Query("page") String pg);

}
