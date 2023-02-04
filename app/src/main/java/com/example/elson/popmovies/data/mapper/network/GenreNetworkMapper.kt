package com.example.elson.popmovies.data.mapper.network

import com.example.elson.popmovies.data.entity.network.GenreNetworkEntity
import com.example.elson.popmovies.data.mapper.Mappers
import com.example.elson.popmovies.data.model.GenreModel

object GenreNetworkMapper : Mappers<GenreNetworkEntity, GenreModel> {

    override fun GenreModel.toEntity(): GenreNetworkEntity {
        return GenreNetworkEntity().let {
            it.name = name
            it
        }
    }

    override fun GenreNetworkEntity.toModel() = GenreModel(name = name)
}
