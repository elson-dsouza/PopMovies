package com.example.elson.popmovies.data.entity

import com.example.elson.popmovies.dagger.app.AppInjector
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieEntity : RealmObject {

    @PrimaryKey
    var id: Long = 0

    var poster: String? = null

    var rating = 0.0

    var title: String? = null

    val isFavorite: Flow<Boolean> get() = AppInjector.appComponent.appDatabase.realm
        .query(MovieEntity::class, "id = $0", id)
        .count().asFlow().map { it > 0 }
}
