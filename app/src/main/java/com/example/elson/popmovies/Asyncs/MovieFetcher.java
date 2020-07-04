package com.example.elson.popmovies.Asyncs;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.elson.popmovies.BuildConfig;
import com.example.elson.popmovies.network.FetchData;
import com.example.elson.popmovies.data.model.MovieHeader;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.elson.popmovies.network.RetrofitGeneratorKt.generateRetrofitForTmdb;

/**
 * Created by Elson on 15-08-2016.
 */
public class MovieFetcher extends AsyncTask<String, Void, MovieHeader> {

    @Nullable
    @Override
    protected MovieHeader doInBackground(String... params) {
        FetchData fetch = generateRetrofitForTmdb().create(FetchData.class);
        Call<MovieHeader> call = fetch.getMovies(params[0], BuildConfig.TMDB_V3_API_TOKEN, params[1]);
        MovieHeader movies = null;
        try {
            movies = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
