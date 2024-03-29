package com.example.elson.popmovies.data.network

import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.data.model.LoggedInUser
import com.example.elson.popmovies.data.model.RequestToken
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface Authentication {

    @POST("/4/auth/request_token")
    fun requestToken(
        @Header("Authorization") token: String = "Bearer ${BuildConfig.TMDB_V4_API_TOKEN}",
        @Body body: JsonObject
    ): Call<RequestToken>

    @POST("/4/auth/access_token")
    fun accessToken(
        @Header("Authorization") token: String = "Bearer ${BuildConfig.TMDB_V4_API_TOKEN}",
        @Body body: JsonObject
    ): Call<LoggedInUser>

    @HTTP(method = "DELETE", path = "/4/auth/access_token", hasBody = true)
    fun logout(
        @Header("Authorization") token: String = "Bearer ${BuildConfig.TMDB_V4_API_TOKEN}",
        @Body body: JsonObject
    ): Call<JsonObject>
}
