package com.example.elson.popmovies.data.mapper.database

import androidx.lifecycle.asLiveData
import com.example.elson.popmovies.data.entity.database.MovieEntity
import com.example.elson.popmovies.data.mapper.Mappers
import com.example.elson.popmovies.data.model.MovieModel

object MovieDataMapper : Mappers<MovieEntity, MovieModel> {

    override fun MovieModel.toEntity(): MovieEntity {
        return MovieEntity().let {
            it.id = id
            it.poster = poster
            it.rating = rating
            it.title = title
            it
        }
    }

    override fun MovieEntity.toModel() = MovieModel(
        id = id,
        poster = poster,
        rating = rating,
        title = title,
        isFavorite = isFavorite.asLiveData()
    )
}
