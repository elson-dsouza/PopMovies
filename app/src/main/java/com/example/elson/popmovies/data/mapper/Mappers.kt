package com.example.elson.popmovies.data.mapper

sealed interface Mappers<Entity : Any, Model : Any> {

    fun Model.toEntity(): Entity

    fun Entity.toModel(): Model
}
