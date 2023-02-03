package com.example.elson.popmovies.ui.navbar

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.webkit.CookieManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.elson.popmovies.R
import com.example.elson.popmovies.compose.collectAsEffect
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.ui.ActivityDestinations
import com.example.elson.popmovies.ui.login.LoginActivity
import com.example.elson.popmovies.ui.movies.grid.MoviesActivity
import kotlinx.coroutines.launch

abstract class BaseNavBarActivity : AppCompatActivity() {

    private val navBarViewModel by viewModels<NavBarViewModel>()
    protected abstract val currentDestination: ActivityDestinations

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    protected fun AppTopBar(
        drawerState: DrawerState,
        snackbarHostState: SnackbarHostState,
        content: @Composable (PaddingValues) -> Unit
    ) = Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = content,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(currentDestination.label)) },
                navigationIcon = {
                    val scope = rememberCoroutineScope()
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, "")
                    }
                }
            )
        }
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    protected fun AppNavigationDrawer(
        drawerState: DrawerState,
        snackbarHostState: SnackbarHostState,
        content: @Composable () -> Unit
    ) = ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        content = {
            content.invoke()
            HandleLogOut(snackbarHostState)
        },
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val scope = rememberCoroutineScope()
                Spacer(Modifier.height(12.dp))
                val items = navBarViewModel.navBarItems.observeAsState(initial = emptyList())
                items.value.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(item) },
                        selected = index == currentDestination.ordinal,
                        onClick = {
                            when (item) {
                                "Movies" -> launch(MoviesActivity::class.java)
                                "Login" -> launch(LoginActivity::class.java)
                                "Logout" -> navBarViewModel.performLogOut()
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    )

    private fun launch(activity: Class<out AppCompatActivity>) {
        val intent = Intent(this, activity)
        intent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT or FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    @Composable
    private fun HandleLogOut(snackbarHostState: SnackbarHostState) {
        val successMessage = stringResource(id = R.string.logout_success)
        val errorMessage = stringResource(id = R.string.logout_failed)
        navBarViewModel.logoutResult.collectAsEffect { result ->
            if (result is Result.Success) {
                CookieManager.getInstance().apply {
                    removeAllCookies(null)
                }
                snackbarHostState.showSnackbar(message = successMessage)
            } else {
                snackbarHostState.showSnackbar(message = errorMessage)
            }
        }
    }
}
