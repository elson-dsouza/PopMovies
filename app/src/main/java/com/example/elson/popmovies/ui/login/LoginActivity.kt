package com.example.elson.popmovies.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.elson.popmovies.R
import com.example.elson.popmovies.ui.movies.PopMovies
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.container
import kotlinx.android.synthetic.main.activity_login.oAuthWebView
import kotlinx.android.synthetic.main.activity_login.progressBar

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                Snackbar.make(container, loginResult.error, Snackbar.LENGTH_LONG).show()
            } else if (loginResult.success == true) {
                val intent = Intent(this, PopMovies::class.java)
                startActivity(intent)
                finish()
            }
        })

        loginViewModel.loginUrl.observe(this@LoginActivity, Observer {
            if (it == null) {
                oAuthWebView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                oAuthWebView.loadUrl(it)
                oAuthWebView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        })

        oAuthWebView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return loginViewModel.handleOAuthRedirect(url)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.startRequestTokenGeneration()
    }

    override fun onStop() {
        super.onStop()
        loginViewModel.stopRequestTokenGeneration()
    }
}