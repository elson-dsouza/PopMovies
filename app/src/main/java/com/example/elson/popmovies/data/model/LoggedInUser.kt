package com.example.elson.popmovies.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        @SerializedName("account_id") val accountId: String,
        @SerializedName("access_token") val accessToken: String
)