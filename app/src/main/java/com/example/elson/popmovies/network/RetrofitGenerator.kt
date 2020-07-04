package com.example.elson.popmovies.network

import com.example.elson.popmovies.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun generateRetrofitForTmdb(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

private val okHttpClient= if (BuildConfig.DEBUG) {
    OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
} else {
    OkHttpClient.Builder()
            .build()
}