package com.example.elson.popmovies.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elson.popmovies.R
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.AuthenticationRepository
import com.example.elson.popmovies.ui.login.LoginActivity
import com.example.elson.popmovies.ui.movies.MoviesActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MoviesActivity::class.java)
        startActivity(intent)
        finish()
    }
}