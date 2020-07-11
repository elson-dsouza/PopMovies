package com.example.elson.popmovies.data

import com.example.elson.popmovies.data.model.LoggedInUser
import com.example.elson.popmovies.data.model.RequestToken
import com.example.elson.popmovies.network.Authentication
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthenticationRepository @Inject constructor(
        private val authentication: Authentication,
        private val securePrefs: SecurePrefs
) {

    var user: LoggedInUser? = null
        private set

    private val userLock = Object()

    init {
        synchronized(userLock) {
            user = securePrefs.getUserCredentials()
        }
    }

    private fun resetCredentials() {
        synchronized(userLock) {
            user = null
            securePrefs.resetUserCredentials()
        }
    }

    fun setLoggedInUser(loggedInUser: LoggedInUser) {
        synchronized(userLock) {
            this.user = loggedInUser
            securePrefs.saveUserCredentials(loggedInUser)
        }
    }

    fun isUserLoggedIn(): Boolean {
        synchronized(userLock) {
            return user != null
        }
    }

    suspend fun generateRequestTokenAsync(
            responseUrl: String
    ): Deferred<Result<RequestToken>> = coroutineScope {
        async(Dispatchers.IO) {
            val body = JsonObject().apply {
                addProperty("redirect_to", responseUrl)
            }
            try {
                val response = authentication.requestToken(body = body)
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
                val response = authentication
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

    suspend fun logoutAsync(): Deferred<Result<JsonObject>> = coroutineScope {
        async(Dispatchers.IO) {
            val body = JsonObject().apply {
                addProperty("access_token", user?.accessToken)
            }
            try {
                val response = authentication
                        .logout(body = body)
                        .execute()
                resetCredentials()
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