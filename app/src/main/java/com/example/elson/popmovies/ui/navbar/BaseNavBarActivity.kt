package com.example.elson.popmovies.ui.navbar

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.elson.popmovies.R
import com.example.elson.popmovies.ui.login.LoginActivity
import com.example.elson.popmovies.ui.movies.MoviesActivity
import com.google.android.material.navigation.NavigationView


abstract class BaseNavBarActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    protected abstract val drawerLayout: DrawerLayout
    protected abstract val navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.content_description_open_navbar, R.string.content_description_close_navbar)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_movies -> launch(MoviesActivity::class.java)
                R.id.item_login -> launch(LoginActivity::class.java)
                //R.id.item_logout -> launch()
            }
            true
        }

        navigationView.inflateHeaderView(R.layout.nav_header)
        navigationView.inflateMenu(R.menu.navigation_menu)
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