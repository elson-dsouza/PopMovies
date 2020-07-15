package com.example.elson.popmovies.data.repository

import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.MovieData
import com.example.elson.popmovies.data.model.MovieFullData
import com.example.elson.popmovies.data.model.MovieListResult
import com.example.elson.popmovies.network.Movies
import io.realm.Realm
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

class MoviesRepository @Inject constructor(
        private val movies: Movies
) {

    suspend fun fetchMoviesAsync(category: String, page: Int):
            Deferred<Result<MovieListResult>> = coroutineScope {
        async(Dispatchers.IO) {
            try {
                val response = movies.getMovies(category,
                        BuildConfig.TMDB_V3_API_TOKEN, page).execute()
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it) } ?: Result.Error(IOException())
                } else {
                    Result.Error(IOException(response.message()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }

    suspend fun toggleFavouriteStateAsync(movie: MovieData)  = coroutineScope {
        async(Dispatchers.IO) {
            Realm.getDefaultInstance()?.use { db ->
                if (movie.isFavorite) {
                    removeFromFavourites(movie, db)
                } else {
                    addToFavourites(movie, db)
                }
            }
        }
    }

    private fun removeFromFavourites(movie: MovieData, db: Realm) {
        db.beginTransaction()
        db.where(MovieData::class.java).equalTo("id", movie.id).findAll().deleteAllFromRealm()
        db.commitTransaction()
    }

    private fun addToFavourites(movie: MovieData, db: Realm) {
        db.beginTransaction()
        db.copyToRealmOrUpdate(movie)
        db.commitTransaction()
    }
}