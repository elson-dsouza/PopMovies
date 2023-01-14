package com.example.elson.popmovies.data.mapper

interface Mappers<Entity : Any, Model : Any> {

    fun Model.toEntity(): Entity

    fun Entity.toModel(): Model
}
