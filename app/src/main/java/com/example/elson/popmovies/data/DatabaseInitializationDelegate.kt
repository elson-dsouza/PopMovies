package com.example.elson.popmovies.data

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

private const val DB_NAME = "MoviesDB"
private const val VERSION = 1L

fun initRealmDb(context: Context, securePrefs: SecurePrefs) {
    Realm.init(context)
    val config = RealmConfiguration.Builder()
            .name(DB_NAME)
            .encryptionKey(securePrefs.getDBKey())
            .schemaVersion(VERSION)
            .build()
    Realm.setDefaultConfiguration(config)
}