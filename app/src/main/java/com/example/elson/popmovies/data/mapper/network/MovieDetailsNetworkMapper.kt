package com.example.elson.popmovies.data.mapper.network

import com.example.elson.popmovies.data.entity.network.MovieDetailsNetworkEntity
import com.example.elson.popmovies.data.mapper.Mappers
import com.example.elson.popmovies.data.mapper.network.GenreNetworkMapper.toEntity
import com.example.elson.popmovies.data.mapper.network.GenreNetworkMapper.toModel
import com.example.elson.popmovies.data.mapper.network.VideoNetworkMapper.toEntity
import com.example.elson.popmovies.data.mapper.network.VideoNetworkMapper.toModel
import com.example.elson.popmovies.data.model.MovieDetailsModel

object MovieDetailsNetworkMapper : Mappers<MovieDetailsNetworkEntity, MovieDetailsModel> {

    override fun MovieDetailsModel.toEntity(): MovieDetailsNetworkEntity {
        return MovieDetailsNetworkEntity().let {
            it.id = id
            it.tagline = tagline
            it.duration = duration
            it.release = release
            it.description = description
            it.isAdult = isAdult
            it.backdrop = backdrop
            it.poster = poster
            it.rawRating = rawRating
            it.title = title
            it.genres = genres.map { genre -> genre.toEntity() }
            it.videos = videos.map { video -> video.toEntity() }
            it
        }
    }

    override fun MovieDetailsNetworkEntity.toModel() = MovieDetailsModel(
        id = id,
        tagline = tagline,
        duration = duration,
        release = release,
        description = description,
        isAdult = isAdult,
        backdrop = backdrop,
        poster = poster,
        rawRating = rawRating,
        title = title,
        genres = genres.map { genre -> genre.toModel() },
        videos = videos.map { video -> video.toModel() }
    )
}
