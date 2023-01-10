package com.example.elson.popmovies.ui.movies.grid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.example.elson.popmovies.R
import com.example.elson.popmovies.data.SecurePrefs
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.data.model.MovieModel
import com.example.elson.popmovies.databinding.ActivityPopMoviesBinding
import com.example.elson.popmovies.ui.movies.detail.MovieDetailActivity
import com.example.elson.popmovies.ui.movies.detail.MovieDetailFragment
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.android.AndroidInjection
import javax.inject.Inject

class MoviesActivity : BaseNavBarActivity(), OnTabSelectedListener {

    private var mode = MovieTypes.POPULAR
    private val activityViewModel by viewModels<MoviesActivityViewModel>()
    private lateinit var binding: ActivityPopMoviesBinding

    @Inject
    lateinit var prefs: SecurePrefs

    override val drawerLayout: DrawerLayout
        get() = binding.navDrawer

    override val navigationView: NavigationView
        get() = binding.navView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pop_movies)
        super.onCreate(savedInstanceState)
        mode = prefs.getMoviesMode()

        binding.tabBar.apply {
            selectTab(getTabAt(mode.ordinal))
            addOnTabSelectedListener(this@MoviesActivity)
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.movieGridFragment, MoviesFragment.newInstance(mode))
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.selectedMovie.observe(this) { movie: MovieModel? ->
            showMovieDetails(movie)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val position = tab.position
        mode = MovieTypes.values()[position]
        prefs.setMoviesMode(mode)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.movieGridFragment, MoviesFragment.newInstance(mode))
            .commit()
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        // Do nothing
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        // Do nothing
    }

    private fun showMovieDetails(movie: MovieModel?) {
        if (movie == null) {
            return
        }
        if (binding.movieDetailFragment == null) {
            startActivity(MovieDetailActivity.getIntent(this, movie))
            activityViewModel.clearSelectedMovie()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.movieDetailFragment, MovieDetailFragment.newInstance(movie))
                .commit()
        }
    }
}
