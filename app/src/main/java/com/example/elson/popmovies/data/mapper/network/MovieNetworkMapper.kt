package com.example.elson.popmovies.data.mapper.network

import androidx.lifecycle.asLiveData
import com.example.elson.popmovies.data.entity.network.MovieNetworkEntity
import com.example.elson.popmovies.data.mapper.Mappers
import com.example.elson.popmovies.data.model.MovieModel

object MovieNetworkMapper : Mappers<MovieNetworkEntity, MovieModel> {

    override fun MovieModel.toEntity(): MovieNetworkEntity {
        return MovieNetworkEntity().let {
            it.id = id
            it.poster = poster
            it.rating = rating
            it.title = title
            it
        }
    }

    override fun MovieNetworkEntity.toModel() = MovieModel(
        id = id,
        poster = poster,
        rating = rating,
        title = title,
        isFavorite = isFavorite.asLiveData()
    )
}
