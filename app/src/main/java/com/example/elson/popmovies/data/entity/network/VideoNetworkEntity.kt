package com.example.elson.popmovies.data.entity.network

import com.google.gson.annotations.SerializedName

data class VideoNetworkEntity(
    @SerializedName("name")
    var name: String = "",

    @SerializedName("key")
    var key: String = "",

    @SerializedName("site")
    var site: String = ""
)
