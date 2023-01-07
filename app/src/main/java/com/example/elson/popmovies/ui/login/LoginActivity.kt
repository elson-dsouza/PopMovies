package com.example.elson.popmovies.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.example.elson.popmovies.R
import com.example.elson.popmovies.databinding.ActivityLoginBinding
import com.example.elson.popmovies.ui.movies.grid.MoviesActivity
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class LoginActivity : BaseNavBarActivity() {

    private lateinit var dataBinding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override val drawerLayout: DrawerLayout
        get() = dataBinding.navDrawer

    override val navigationView: NavigationView
        get() = dataBinding.navView

    override fun onCreate(savedInstanceState: Bundle?) {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        super.onCreate(savedInstanceState)

        loginViewModel.loginResult.observe(
            this@LoginActivity,
            Observer {
                val loginResult = it ?: return@Observer
                if (loginResult.error != null) {
                    Snackbar.make(dataBinding.container, loginResult.error, Snackbar.LENGTH_LONG).show()
                } else if (loginResult.success == true) {
                    val intent = Intent(this, MoviesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    finish()
                }
            }
        )

        loginViewModel.loginUrl.observe(
            this@LoginActivity,
            Observer {
                if (it == null) {
                    dataBinding.oAuthWebView.visibility = View.GONE
                    dataBinding.progressBar.visibility = View.VISIBLE
                } else {
                    dataBinding.oAuthWebView.loadUrl(it)
                    dataBinding.oAuthWebView.visibility = View.VISIBLE
                    dataBinding.progressBar.visibility = View.GONE
                }
            }
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dataBinding.oAuthWebView.isForceDarkAllowed = true
        }
        dataBinding.oAuthWebView.setBackgroundColor(Color.TRANSPARENT)
        dataBinding.oAuthWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                loginViewModel.handleOAuthRedirect(request.url.toString())
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
