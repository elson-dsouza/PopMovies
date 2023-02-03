package com.example.elson.popmovies.ui.login

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.elson.popmovies.compose.collectAsEffect
import com.example.elson.popmovies.theme.AppTheme
import com.example.elson.popmovies.ui.ActivityDestinations
import com.example.elson.popmovies.ui.movies.grid.MoviesActivity
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

class LoginActivity : BaseNavBarActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()

    override val currentDestination: ActivityDestinations
        get() = ActivityDestinations.LOGIN

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            AppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                AppNavigationDrawer(drawerState = drawerState, snackBarHostState) {
                    AppTopBar(drawerState = drawerState, snackBarHostState) { padding ->
                        val state = rememberWebViewState(loginViewModel.loginUrl.observeAsState(initial = "").value)
                        WebView(
                            state = state,
                            modifier = Modifier.padding(padding).fillMaxSize(),
                            client = remember { LoginWebViewClient(loginViewModel) }
                        )
                        if (state.isLoading) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize(),
                                content = { CircularProgressIndicator() }
                            )
                        }

                        loginViewModel.loginResult.collectAsEffect { loginResult ->
                            if (loginResult.error != null) {
                                snackBarHostState.showSnackbar(message = loginResult.error)
                            } else if (loginResult.success == true) {
                                val intent = Intent(this, MoviesActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    class LoginWebViewClient(private val loginViewModel: LoginViewModel) : AccompanistWebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) =
            loginViewModel.handleOAuthRedirect(request?.url.toString()) || super.shouldOverrideUrlLoading(view, request)
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
