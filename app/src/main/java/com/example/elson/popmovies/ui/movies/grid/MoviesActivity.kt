package com.example.elson.popmovies.ui.movies.grid

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.elson.popmovies.compose.FragmentContainer
import com.example.elson.popmovies.data.enumeration.MovieTypes
import com.example.elson.popmovies.theme.AppTheme
import com.example.elson.popmovies.ui.ActivityDestinations
import com.example.elson.popmovies.ui.movies.detail.MovieDetailActivity
import com.example.elson.popmovies.ui.movies.detail.MovieDetailFragment
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity

class MoviesActivity : BaseNavBarActivity() {

    private val viewModel by viewModels<MoviesActivityViewModel>()

    override val currentDestination: ActivityDestinations
        get() = ActivityDestinations.MOVIES

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            AppTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                AppNavigationDrawer(drawerState = drawerState, snackBarHostState) {
                    AppTopBar(drawerState = drawerState, snackBarHostState) { padding ->
                        val windowSizeClass = calculateWindowSizeClass(this)
                        val modifier = Modifier.padding(padding)
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            MoviesGridSinglePane(modifier)
                        } else {
                            MoviesGridDualPane(modifier)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun MoviesGridSinglePane(modifier: Modifier) {
        MoviesGridView(modifier)
        val selectedMovie = viewModel.selectedMovie.observeAsState().value
        if (selectedMovie != null) {
            startActivity(MovieDetailActivity.getIntent(this, selectedMovie))
            viewModel.clearSelectedMovie()
        }
    }

    @Composable
    private fun MoviesGridDualPane(modifier: Modifier) {
        Row(modifier) {
            MoviesGridView(Modifier.width(334.dp))
            // Crossfade between different detail posts
            Crossfade(targetState = viewModel.selectedMovie.observeAsState()) { selectedMovie ->
                // Get the lazy list state for this detail view
                selectedMovie.value?.let { movie ->
                    FragmentContainer(fragmentManager = supportFragmentManager) { id ->
                        replace(id, MovieDetailFragment.newInstance(movie))
                    }
                }
            }
        }
    }

    @Composable
    private fun MoviesGridView(modifier: Modifier = Modifier) {
        Column(modifier = modifier) {
            val movieMode = viewModel.currentMode.collectAsState()
            val selectedIndex = movieMode.value.ordinal
            TabRow(selectedTabIndex = selectedIndex) {
                MovieTypes.values().forEachIndexed { index, type ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { viewModel.updateMovieMode(type) },
                        text = { Text(stringResource(id = type.nameRes)) }
                    )
                }
            }
            MoviesGridView(viewModel.pagingDataFlow, viewModel::loadDetails, viewModel::toggleFavouriteState)
        }
    }
}
