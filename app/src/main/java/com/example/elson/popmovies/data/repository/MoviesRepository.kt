package com.example.elson.popmovies.data.repository

import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.model.MovieListResult
import com.example.elson.popmovies.network.Movies
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
            val response = movies.getMovies(category,
                    BuildConfig.TMDB_V3_API_TOKEN, page).execute()
            try {
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
}