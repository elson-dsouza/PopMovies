package com.example.elson.popmovies.data.mapper.network

import com.example.elson.popmovies.data.entity.network.VideoNetworkEntity
import com.example.elson.popmovies.data.mapper.Mappers
import com.example.elson.popmovies.data.model.VideoModel

object VideoNetworkMapper : Mappers<VideoNetworkEntity, VideoModel> {

    override fun VideoModel.toEntity(): VideoNetworkEntity {
        return VideoNetworkEntity().let {
            it.name = name
            it.key = key
            it.site = site
            it
        }
    }

    override fun VideoNetworkEntity.toModel() = VideoModel(name = name, key = key, site = site)
}
