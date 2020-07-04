package com.example.elson.popmovies.ui.login

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.elson.popmovies.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.container
import kotlinx.android.synthetic.main.activity_login.oAuthWebView

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
                finish()
            }
        })

        loginViewModel.loginUrl.observe(this@LoginActivity, Observer {
            val url = it ?: return@Observer
            oAuthWebView.loadUrl(url)
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