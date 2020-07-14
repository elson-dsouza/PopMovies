package com.example.elson.popmovies.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elson.popmovies.ui.movies.MoviesActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MoviesActivity::class.java)
        startActivity(intent)
        finish()
    }
}