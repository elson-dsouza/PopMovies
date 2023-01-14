package com.example.elson.popmovies.data.model

import android.os.Parcelable
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.entity.database.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Elson on 10-10-2016.
 */
@Parcelize
class FullMovieModel(
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
    var genres: List<Genre>? = null,

    @SerializedName("videos")
    var videos: List<Video>? = null
) : Parcelable {

    fun getDurationString(): String {
        return "$duration min"
    }

    fun getRating(): String {
        return "$rawRating/10"
    }

    fun getGenresList(): List<String> {
        return genres?.map { it.name } ?: emptyList()
    }

    fun getVideosList(): List<Video> {
        return videos!!
    }

    val isFavorite: Boolean
        get() = AppInjector.appComponent.appDatabase.realm
            .query(MovieEntity::class, "id = $0", id).first().find() != null
}
