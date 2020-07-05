package com.example.elson.popmovies.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.elson.popmovies.MoviesApplication
import javax.inject.Inject

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
}