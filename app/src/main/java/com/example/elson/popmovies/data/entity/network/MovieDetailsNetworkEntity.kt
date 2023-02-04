package com.example.elson.popmovies.data.entity.network

import com.google.gson.annotations.SerializedName

data class MovieDetailsNetworkEntity(
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("tagline")
    var tagline: String? = null,

    @SerializedName("runtime")
    var duration: Int = 0,

    @SerializedName("release_date")
    var release: String? = null,

    @SerializedName("overview")
    var description: String? = null,

    @SerializedName("adult")
    var isAdult: Boolean = false,

    @SerializedName("backdrop_path")
    var backdrop: String? = null,

    @SerializedName("poster_path")
    var poster: String? = null,

    @SerializedName("vote_average")
    var rawRating: Double = 0.0,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("genres")
    var genres: List<GenreNetworkEntity> = emptyList(),

    @SerializedName("videos")
    var videos: List<VideoNetworkEntity> = emptyList()
)
