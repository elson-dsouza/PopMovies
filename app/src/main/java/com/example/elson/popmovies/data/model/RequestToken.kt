package com.example.elson.popmovies.data.model

import com.google.gson.annotations.SerializedName

data class RequestToken(
    @SerializedName("request_token") val requestToken: String
)
