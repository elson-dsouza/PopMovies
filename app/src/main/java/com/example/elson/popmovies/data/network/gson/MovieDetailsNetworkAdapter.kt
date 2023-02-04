package com.example.elson.popmovies.data.network.gson

import com.example.elson.popmovies.data.entity.network.VideoNetworkEntity
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MovieDetailsNetworkAdapter : JsonDeserializer<List<VideoNetworkEntity>> {

    companion object {
        fun getTypeToken(): Type {
            return object : TypeToken<List<VideoNetworkEntity>>() {}.type
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): List<VideoNetworkEntity> {
        json ?: return mutableListOf()
        val jsonArray = if (json.isJsonObject) {
            (json as JsonObject).getAsJsonArray("results")
        } else {
            json.asJsonArray
        }
        return jsonArray.map {
            context.deserialize(it, VideoNetworkEntity::class.java)
        }
    }
}
