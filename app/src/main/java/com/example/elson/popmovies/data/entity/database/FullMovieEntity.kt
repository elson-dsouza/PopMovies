package com.example.elson.popmovies.data.entity.database

import com.example.elson.popmovies.data.model.Genre
import com.example.elson.popmovies.data.model.Video
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class FullMovieEntity : RealmObject {

    @SerializedName("tagline")
    var tagline: String? = null

    @SerializedName("runtime")
    var duration = 0

    @SerializedName("release_date")
    var release: String? = null

    @SerializedName("overview")
    var description: String? = null

    @SerializedName("adult")
    var isAdult = false

    @SerializedName("backdrop_path")
    var backdrop: String? = null

    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0

    @SerializedName("poster_path")
    var poster: String? = null

    @SerializedName("vote_average")
    var rawRating = 0.0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("genres")
    var genres: RealmList<Genre>? = null

    @SerializedName("videos")
    var videos: RealmList<Video>? = null
}
