package com.example.elson.popmovies.async;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.data.model.ReviewsHeader;
import com.example.elson.popmovies.network.FetchData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elson on 19-10-2016.
 */
public class ReviewFetcher extends AsyncTask<String, Void, ReviewsHeader> {
    @Nullable
    @Override
    protected ReviewsHeader doInBackground(String... params) {
        FetchData fetch = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FetchData.class);
        Call<ReviewsHeader> call = fetch.getReviews(params[0], BuildConfig.TMDB_V3_API_TOKEN, params[1]);
        ReviewsHeader reviews = null;
        try {
            reviews = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
