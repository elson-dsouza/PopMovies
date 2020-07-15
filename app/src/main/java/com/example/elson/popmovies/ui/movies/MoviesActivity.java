package com.example.elson.popmovies.ui.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.SecurePrefs;
import com.example.elson.popmovies.data.enumeration.MovieTypes;
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.realm.Realm;

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

    @Inject
    SecurePrefs prefs;

    @NonNull
    private MovieTypes mode = MovieTypes.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_pop_movies);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mode = prefs.getMoviesMode();
        tabBar.selectTab(tabBar.getTabAt(mode.ordinal()));
        tabBar.addOnTabSelectedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movieGridFragment, MoviesFragment.newInstance(mode))
                .commit();
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
}
