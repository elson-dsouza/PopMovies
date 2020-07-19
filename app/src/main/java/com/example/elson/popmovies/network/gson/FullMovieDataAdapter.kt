package com.example.elson.popmovies.network.gson

import com.example.elson.popmovies.data.model.Video
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.realm.RealmList
import java.lang.reflect.Type

class FullMovieDataAdapter: JsonDeserializer<RealmList<Video>> {

    companion object {
        fun getTypeToken(): Type {
            return object : TypeToken<RealmList<Video>>() {}.type
        }
    }

    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext
    ): RealmList<Video> {
        json ?: return RealmList()
        val jsonArray = if (json.isJsonObject) {
            (json as JsonObject).getAsJsonArray("results")
        } else  {
            json.asJsonArray
        }
        val videoList = jsonArray.map {
            context.deserialize<Video>(it, Video::class.java) }.toTypedArray()
        return RealmList(*videoList)
    }
}
