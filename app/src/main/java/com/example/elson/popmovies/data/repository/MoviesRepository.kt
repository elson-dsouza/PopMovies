package com.example.elson.popmovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.dagger.movies.MoviesScope
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.SecureDatabase
import com.example.elson.popmovies.data.entity.MovieEntity
import com.example.elson.popmovies.data.mapper.MovieDataMapper.toEntity
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.network.Movies
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

@MoviesScope
class MoviesRepository @Inject constructor(
    private val appDatabase: SecureDatabase,
    private val movies: Movies
) {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun fetchMoviesList(category: String): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPageSource(category, movies) }
        ).flow
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
