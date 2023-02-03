package com.example.elson.popmovies.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elson.popmovies.ui.movies.grid.MoviesActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MoviesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
