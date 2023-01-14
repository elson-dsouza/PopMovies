package com.example.elson.popmovies.data.entity.network

import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.entity.database.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class MovieNetworkEntity(
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("poster_path")
    var poster: String? = null,

    @SerializedName("vote_average")
    var rating: Double = 0.0,

    @SerializedName("title")
    var title: String? = null
) {
    val isFavorite: Flow<Boolean> get() = AppInjector.appComponent.appDatabase.realm
        .query(MovieEntity::class, "id = $0", id)
        .count().asFlow().map { it > 0 }
}
