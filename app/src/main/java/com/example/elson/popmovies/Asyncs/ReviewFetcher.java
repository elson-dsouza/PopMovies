package com.example.elson.popmovies.Asyncs;

import android.os.AsyncTask;

import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.FetchData;
import com.example.elson.popmovies.pojo.ReviewsHeader;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elson on 19-10-2016.
 */
public class ReviewFetcher extends AsyncTask<String, Void, ReviewsHeader> {
    @Override
    protected ReviewsHeader doInBackground(String... params) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FetchData fetch = retrofit.create(FetchData.class);
        Call<ReviewsHeader> call = fetch.getReviews(params[0], "BuildConfig.API_KEY", params[1]);
        ReviewsHeader reviews = null;
        try {
            reviews = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
