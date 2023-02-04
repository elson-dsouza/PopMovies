package com.example.elson.popmovies.data.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Synchronized
fun getNetworkGson(): Gson = GsonBuilder()
    .registerTypeAdapter(MovieDetailsNetworkAdapter.getTypeToken(), MovieDetailsNetworkAdapter())
    .create()
