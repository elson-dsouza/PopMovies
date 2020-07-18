package com.example.elson.popmovies.ui.movies.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.model.MovieData

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun getIntent(callingActivity: Activity, movie: MovieData) =
                Intent(callingActivity, MovieDetailActivity::class.java).apply {
                    putExtra(ARG_MOVIE_DATA, movie)
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        val movie = intent.getParcelableExtra<MovieData>(ARG_MOVIE_DATA)
                ?: error("No movie passed in")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MovieDetailFragment.newInstance(movie))
                    .commit()
        }
    }
}