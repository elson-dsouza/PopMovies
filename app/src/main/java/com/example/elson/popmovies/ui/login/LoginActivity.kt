package com.example.elson.popmovies.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.elson.popmovies.R
import com.example.elson.popmovies.ui.movies.grid.MoviesActivity
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.container
import kotlinx.android.synthetic.main.activity_login.navDrawer
import kotlinx.android.synthetic.main.activity_login.navView
import kotlinx.android.synthetic.main.activity_login.oAuthWebView
import kotlinx.android.synthetic.main.activity_login.progressBar

class LoginActivity : BaseNavBarActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override val drawerLayout: DrawerLayout
        get() = navDrawer

    override val navigationView: NavigationView
        get() = navView

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                Snackbar.make(container, loginResult.error, Snackbar.LENGTH_LONG).show()
            } else if (loginResult.success == true) {
                val intent = Intent(this, MoviesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
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