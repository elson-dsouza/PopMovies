package com.example.elson.popmovies.network.gson

import com.example.elson.popmovies.data.model.Video
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FullMovieDataAdapter : JsonDeserializer<List<Video>> {

    companion object {
        fun getTypeToken(): Type {
            return object : TypeToken<List<Video>>() {}.type
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): List<Video> {
        json ?: return mutableListOf()
        val jsonArray = if (json.isJsonObject) {
            (json as JsonObject).getAsJsonArray("results")
        } else {
            json.asJsonArray
        }
        return jsonArray.map {
            context.deserialize(it, Video::class.java)
        }
    }
}
