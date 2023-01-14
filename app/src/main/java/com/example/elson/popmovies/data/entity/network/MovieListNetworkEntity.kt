package com.example.elson.popmovies.data.entity.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Elson on 21-05-2016.
 */
data class MovieListNetworkEntity(
    @SerializedName("page")
    var current: Int = 0,

    @SerializedName("results")
    var result: List<MovieNetworkEntity> = emptyList(),

    @SerializedName("total_pages")
    var total: Int = 0
)
