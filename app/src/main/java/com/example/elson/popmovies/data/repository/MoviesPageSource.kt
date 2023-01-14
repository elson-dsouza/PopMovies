package com.example.elson.popmovies.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.elson.popmovies.BuildConfig
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.data.entity.network.MovieListNetworkEntity
import com.example.elson.popmovies.data.mapper.network.MovieNetworkMapper.toModel
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.network.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

// The initial key used for loading. Ensures that we do not try to load a negative page
private const val STARTING_KEY = 1

class MoviesPageSource(
    private val category: String,
    private val service: Movies
) : PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        // Start paging with the STARTING_KEY if this is the first load
        val currentPage = params.key ?: STARTING_KEY
        val result = fetchMoviesForPage(currentPage)

        if (result !is Result.Success) {
            return LoadResult.Error((result as Result.Error).exception)
        }

        return result.data.let {
            val movieModelList = it.result.map { entity -> entity.toModel() }
            LoadResult.Page(
                data = movieModelList,
                prevKey = it.current.getPrevPage(),
                nextKey = it.current.getNextPage(result.data.total)
            )
        }
    }

    private suspend fun fetchMoviesForPage(page: Int): Result<MovieListNetworkEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getMovies(
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

    private fun Int.getPrevPage(): Int? = when {
        this > STARTING_KEY -> this - 1
        else -> null
    }

    private fun Int.getNextPage(totalPages: Int): Int? = when {
        this < STARTING_KEY -> STARTING_KEY
        this < totalPages -> this + 1
        else -> null
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>) = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.nextKey
    }
}
