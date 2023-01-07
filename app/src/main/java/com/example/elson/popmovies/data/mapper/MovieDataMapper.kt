package com.example.elson.popmovies.data.mapper

import androidx.lifecycle.asLiveData
import com.example.elson.popmovies.data.entity.MovieEntity
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

    override fun MovieEntity.toModel(): MovieModel {
        return MovieModel().let {
            it.id = id
            it.poster = poster
            it.rating = rating
            it.title = title
            it.isFavorite = isFavorite.asLiveData()
            it
        }
    }
}
