package com.example.elson.popmovies.ui.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;

import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.SecurePrefs;
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
    private String query = "popular";

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_pop_movies);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
        query = prefs.getMoviesQuery();

        tabBar.selectTab(tabBar.getTabAt(getSelectedTabIndex()));
        tabBar.addOnTabSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movieGridFragment, MoviesFragment.newInstance(query))
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private int getSelectedTabIndex() {
        if (query.equalsIgnoreCase("popular")) {
            return 0;
        } else if (query.equalsIgnoreCase("top_rated")) {
            return 1;
        } else {
            return 2;
        }
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
        if (position == 0) {
            query = "popular";
        } else if (position == 1) {
            query = "now_playing";
        } else if (position == 2) {
            query = "upcoming";
        } else {
            query = "top_rated";
        }
        prefs.putMoviesQuery(query);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movieGridFragment, MoviesFragment.newInstance(query))
                .commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
