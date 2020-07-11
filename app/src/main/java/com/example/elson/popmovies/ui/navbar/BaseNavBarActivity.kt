package com.example.elson.popmovies.ui.navbar

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.elson.popmovies.R
import com.example.elson.popmovies.dagger.app.AppInjector
import com.example.elson.popmovies.data.AuthenticationRepository
import com.example.elson.popmovies.data.Result
import com.example.elson.popmovies.ui.login.LoginActivity
import com.example.elson.popmovies.ui.movies.MoviesActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class BaseNavBarActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    protected abstract val drawerLayout: DrawerLayout
    protected abstract val navigationView: NavigationView
    protected abstract val rootLayout: View
    @Inject lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppInjector.getAuthenticationSubComponent().inject(this)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.content_description_open_navbar, R.string.content_description_close_navbar)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_movies -> launch(MoviesActivity::class.java)
                R.id.item_login -> launch(LoginActivity::class.java)
                R.id.item_logout -> handleLogOut()
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        refreshNavBar()
    }

    private fun refreshNavBar() {
        if (::headerView.isInitialized) {
            navigationView.removeHeaderView(headerView)
            navigationView.menu.clear()
        }
        headerView = navigationView.inflateHeaderView(R.layout.nav_header)
        navigationView.inflateMenu(R.menu.navigation_menu)
        if (authenticationRepository.isUserLoggedIn()) {
            navigationView.menu.removeItem(R.id.item_login)
        } else {
            navigationView.menu.removeItem(R.id.item_logout)
        }
    }

    private fun handleLogOut() {
        MainScope().launch {
            val result = authenticationRepository.logoutAsync().await()
            refreshNavBar()
            if (result is Result.Success) {
                Snackbar.make(rootLayout, R.string.logout_success, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(rootLayout, R.string.logout_failed, Snackbar.LENGTH_LONG).show()
            }
        }
        drawerLayout.closeDrawers()
    }

    private fun launch(activity: Class<out AppCompatActivity>) {
        drawerLayout.closeDrawers()
        val intent = Intent(this, activity)
        intent.flags = FLAG_ACTIVITY_REORDER_TO_FRONT or FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        actionBarDrawerToggle.onOptionsItemSelected(item) or super.onOptionsItemSelected(item)
}