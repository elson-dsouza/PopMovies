package com.example.elson.popmovies.Asyncs;

import android.os.AsyncTask;

import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.FetchData;
import com.example.elson.popmovies.pojo.VideoResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Elson on 11-10-2016.
 */
public class VideoFetcher extends AsyncTask<String, Void, VideoResponse> {

    @Override
    protected VideoResponse doInBackground(String... params) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FetchData fetch = retrofit.create(FetchData.class);
        Call<VideoResponse> call = fetch.getVideos(params[0], BuildConfig.API_KEY);
        VideoResponse videos = null;
        try {
            videos = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videos;
    }
}
