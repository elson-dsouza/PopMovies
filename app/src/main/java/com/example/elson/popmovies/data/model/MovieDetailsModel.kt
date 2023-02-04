package com.example.elson.popmovies.data.model

import android.os.Parcelable
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.entity.database.MovieEntity
import kotlinx.parcelize.Parcelize

/**
 * Created by Elson on 10-10-2016.
 */
@Parcelize
class MovieDetailsModel(
    val id: Long,
    val tagline: String?,
    val duration: Int,
    val release: String?,
    val description: String?,
    val isAdult: Boolean,
    val backdrop: String?,
    val poster: String?,
    val rawRating: Double,
    val title: String?,
    val genres: List<GenreModel>,
    val videos: List<VideoModel>
) : Parcelable {

    fun getDurationString(): String {
        return "$duration min"
    }

    fun getRating(): String {
        return "$rawRating/10"
    }

    fun getGenresList(): List<String> {
        return genres.map { it.name }
    }

    val isFavorite: Boolean
        get() = AppInjector.appComponent.appDatabase.realm
            .query(MovieEntity::class, "id = $0", id).first().find() != null
}
