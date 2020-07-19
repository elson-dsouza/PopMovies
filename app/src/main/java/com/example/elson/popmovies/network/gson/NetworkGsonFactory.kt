package com.example.elson.popmovies.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Synchronized
fun getNetworkGson(): Gson = GsonBuilder()
        .registerTypeAdapter(FullMovieDataAdapter.getTypeToken(), FullMovieDataAdapter())
        .create()