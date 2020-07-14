package com.example.elson.popmovies.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.elson.popmovies.data.model.LoggedInUser
import com.google.gson.Gson
import java.security.SecureRandom
import javax.inject.Inject

private const val KEY_USER_CREDENTIALS = "user_credentials"
private const val DB_KEY = "db_key"
private const val MOVIES_QUERY = "movies_query"

class SecurePrefs @Inject constructor(context: Context) {

    private val masterKeyAlias: String
    private val sharedPreferences: SharedPreferences
    private val fileName = "SecurePrefsFile"

    init {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
                fileName, masterKeyAlias, context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getDBKey(): ByteArray {
        return if(sharedPreferences.contains(DB_KEY)) {
            Base64.decode(sharedPreferences.getString(DB_KEY, ""), Base64.DEFAULT)
        } else {
            val key = ByteArray(64)
            SecureRandom().nextBytes(key)
            sharedPreferences.edit()
                    .putString(DB_KEY, Base64.encodeToString(key, Base64.DEFAULT)).apply()
            key
        }
    }

    fun saveUserCredentials(loggedInUser: LoggedInUser) {
        val userCredJson = Gson().toJson(loggedInUser)
        sharedPreferences.edit().putString(KEY_USER_CREDENTIALS, userCredJson).apply()
    }

    fun getUserCredentials(): LoggedInUser? {
        val userCredJson = sharedPreferences.getString(KEY_USER_CREDENTIALS, "")
        return Gson().fromJson(userCredJson, LoggedInUser::class.java)
    }

    fun resetUserCredentials() {
        sharedPreferences.edit().remove(KEY_USER_CREDENTIALS).apply()
    }

    fun getMoviesQuery(): String {
        return sharedPreferences.getString(MOVIES_QUERY,"popular")!!
    }

    fun putMoviesQuery(query: String) {
        return sharedPreferences.edit().putString(MOVIES_QUERY,query).apply()
    }
}