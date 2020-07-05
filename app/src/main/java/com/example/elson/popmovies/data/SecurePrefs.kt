package com.example.elson.popmovies.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.elson.popmovies.data.model.LoggedInUser
import com.google.gson.Gson
import javax.inject.Inject

class SecurePrefs @Inject constructor(context: Context) {

    private val masterKeyAlias: String
    private val sharedPreferences: SharedPreferences
    private val fileName = "SecurePrefsFile"

    private val KEY_USER_CREDENTIALS = "user_credentials"

    init {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
                fileName, masterKeyAlias, context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveUserCredentials(loggedInUser: LoggedInUser) {
        val userCredsJson = Gson().toJson(loggedInUser)
        sharedPreferences.edit().putString(KEY_USER_CREDENTIALS, userCredsJson).apply()
    }

    fun getUserCredentials(): LoggedInUser? {
        val userCredsJson = sharedPreferences.getString(KEY_USER_CREDENTIALS, "")
        return Gson().fromJson(userCredsJson, LoggedInUser::class.java)
    }

    fun resetUserCredentials() {
        sharedPreferences.edit().remove(KEY_USER_CREDENTIALS).apply()
    }
}