package com.example.elson.popmovies.data

import com.example.elson.popmovies.data.model.LoggedInUser
import com.example.elson.popmovies.data.model.RequestToken
import com.example.elson.popmovies.network.Authentication
import com.example.elson.popmovies.network.generateRetrofitForTmdb
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
object AuthenticationRepository {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
    }

    fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun generateRequestTokenAsync(
            responseUrl: String
    ): Deferred<Result<RequestToken>> = coroutineScope {
        async(Dispatchers.IO) {
            val body = JsonObject().apply {
                addProperty("redirect_to", responseUrl)
            }
            try {
                val response = generateRetrofitForTmdb()
                        .create(Authentication::class.java)
                        .requestToken(body = body)
                        .execute()
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it) } ?: Result.Error(IOException())
                } else {
                    Result.Error(IOException(response.errorBody()?.string()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }

        }
    }

    suspend fun requestAccessTokenAsync(
            requestToken: String
    ): Deferred<Result<LoggedInUser>> = coroutineScope {
        async(Dispatchers.IO) {
            val body = JsonObject().apply {
                addProperty("request_token", requestToken)
            }
            try {
                val response = generateRetrofitForTmdb()
                        .create(Authentication::class.java)
                        .accessToken(body = body)
                        .execute()
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it) } ?: Result.Error(IOException())
                } else {
                    Result.Error(IOException(response.errorBody()?.string()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }
}