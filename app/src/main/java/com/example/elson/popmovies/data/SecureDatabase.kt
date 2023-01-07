package com.example.elson.popmovies.data

import com.example.elson.popmovies.data.entity.FullMovieEntity
import com.example.elson.popmovies.data.entity.MovieEntity
import com.example.elson.popmovies.data.model.Genre
import com.example.elson.popmovies.data.model.Video
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureDatabase @Inject constructor(securePrefs: SecurePrefs) {

    val realm: Realm

    private val dbName = "MoviesDB"
    private val dbVersion = 2L

    private val schema = setOf(FullMovieEntity::class, MovieEntity::class, Genre::class, Video::class)
    private val config = RealmConfiguration.Builder(schema = schema)
        .name(dbName)
        .encryptionKey(securePrefs.getDBKey())
        .schemaVersion(dbVersion)
        .build()

    init {
        realm = Realm.open(config)
    }
}
