package com.example.elson.popmovies.data.repository

import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.dagger.movies.MoviesScope
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.SecureDatabase
import com.example.elson.popmovies.data.entity.MovieEntity
import com.example.elson.popmovies.data.mapper.MovieDataMapper.toEntity
import com.example.elson.popmovies.data.model.MovieListResult
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.network.Movies
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import javax.inject.Inject

@MoviesScope
class MoviesRepository @Inject constructor(
    private val appDatabase: SecureDatabase,
    private val movies: Movies
) {

    suspend fun fetchMoviesAsync(category: String, page: Int):
        Deferred<Result<MovieListResult>> = coroutineScope {
        async(Dispatchers.IO) {
            try {
                val response = movies.getMovies(
                    category,
                    BuildConfig.TMDB_V3_API_TOKEN,
                    page
                ).execute()
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

    suspend fun toggleFavouriteStateAsync(movie: MovieModel) =
        appDatabase.realm.write {
            if (movie.isFavorite.value == true) {
                removeFromFavourites(movie)
            } else {
                addToFavourites(movie)
            }
        }

    private fun MutableRealm.removeFromFavourites(movie: MovieModel) {
        val movies = query(MovieEntity::class).query("id = $0", movie.id).find()
        delete(movies)
    }

    private fun MutableRealm.addToFavourites(movie: MovieModel) {
        copyToRealm(movie.toEntity(), updatePolicy = UpdatePolicy.ALL)
    }

    suspend fun fetchMovieDetailsAsync(id: Long) = coroutineScope {
        async(Dispatchers.IO) {
            try {
                val response = movies.getData(
                    id,
                    BuildConfig.TMDB_V3_API_TOKEN,
                    "videos"
                ).execute()
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it) } ?: Result.Error(IOException())
                } else {
                    Result.Error(IOException(response.message()))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
