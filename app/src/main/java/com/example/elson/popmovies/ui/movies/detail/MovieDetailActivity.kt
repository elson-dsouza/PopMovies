package com.example.elson.popmovies.ui.movies.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elson.popmovies.R

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MovieDetailFragment.newInstance())
                    .commit()
        }
    }
}