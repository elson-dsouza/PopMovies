package com.example.elson.popmovies.data.entity.network

import com.google.gson.annotations.SerializedName

data class GenreNetworkEntity(
    @SerializedName("name")
    var name: String = ""
)
