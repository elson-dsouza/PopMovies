package com.example.elson.popmovies.ui.movies.grid;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.SecurePrefs;
import com.example.elson.popmovies.data.enumeration.MovieTypes;
import com.example.elson.popmovies.data.model.MovieModel;
import com.example.elson.popmovies.ui.movies.detail.MovieDetailActivity;
import com.example.elson.popmovies.ui.movies.detail.MovieDetailFragment;
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MoviesActivity extends BaseNavBarActivity implements TabLayout.OnTabSelectedListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tabBar)
    TabLayout tabBar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.navDrawer)
    DrawerLayout navDrawer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.navView)
    NavigationView navView;

    @Nullable
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.movieDetailFragment)
    FragmentContainerView movieDetailFragment;


    @Inject
    SecurePrefs prefs;

    @NonNull
    private MovieTypes mode = MovieTypes.POPULAR;

    private MoviesActivityViewModel activityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_pop_movies);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mode = prefs.getMoviesMode();
        tabBar.selectTab(tabBar.getTabAt(mode.ordinal()));
        tabBar.addOnTabSelectedListener(this);
        activityViewModel = (new ViewModelProvider(this)).get(MoviesActivityViewModel.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movieGridFragment, MoviesFragment.newInstance(mode))
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityViewModel.getSelectedMovie().observe(this, this::showMovieDetails);
    }

    @Override
    public void onBackPressed() {
//        CustomYoutubeFragment youTubePlayerFragment = (CustomYoutubeFragment)
//                getFragmentManager().findFragmentByTag(VideoAdapter.PLAYER);
//        if (youTubePlayerFragment != null) {
//            if (youTubePlayerFragment.isFullScreen()) {
//                youTubePlayerFragment.setFullScreen(false);
//                return;
//            }
//        }
        super.onBackPressed();
    }

    @NonNull
    @Override
    public DrawerLayout getDrawerLayout() {
        return navDrawer;
    }

    @NonNull
    @Override
    public NavigationView getNavigationView() {
        return navView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mode = MovieTypes.values()[position];
        prefs.setMoviesMode(mode);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movieGridFragment, MoviesFragment.newInstance(mode))
                .commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    protected void showMovieDetails(@Nullable MovieModel movie) {
        if (movie == null) {
            return;
        }
        if (movieDetailFragment == null) {
            startActivity(MovieDetailActivity.getIntent(this, movie));
            activityViewModel.clearSelectedMovie();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movieDetailFragment, MovieDetailFragment.newInstance(movie))
                    .commit();
        }
    }
}
