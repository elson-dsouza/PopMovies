package com.example.elson.popmovies.ui.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.elson.popmovies.R;
import com.example.elson.popmovies.data.model.MovieFullData;
import com.example.elson.popmovies.data.model.MovieHeader;
import com.example.elson.popmovies.network.MovieFetcher;
import com.example.elson.popmovies.ui.navbar.BaseNavBarActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.paginate.Paginate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MoviesActivity extends BaseNavBarActivity implements Paginate.Callbacks {

    private static final String MOVIELIST = "MOVIEKEY";
    private static final String QUERY = "QUERYKEY";
    private final int NUM_COLS = 2;

    @BindView(R.id.movieGrid)
    RecyclerView movieGridView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.tabBar)
    TabLayout tabBar;

    @BindView(R.id.navDrawer)
    DrawerLayout navDrawer;

    @BindView(R.id.navView)
    NavigationView navView;

    private ArrayList<Parcelable> movieList;
    private GridAdapter movieListAdapter;
    private GridLayoutManager movieLayoutManager;
    @Nullable
    private String query="popular";
    private boolean mtwoPane;
    private int currentPgNo=1;
    private int totalPgNo=1;
    private boolean loading = false;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pop_movies);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();
        mtwoPane = container != null;

        if (savedInstanceState != null) {
            movieList = savedInstanceState.getParcelableArrayList(MOVIELIST);
            query = savedInstanceState.getString(QUERY);
            movieListAdapter = new GridAdapter(movieList, mtwoPane, getFragmentManager(), realm);
            movieGridView.setAdapter(movieListAdapter);
        } else {
            movieList = new ArrayList<>();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            query = preferences.getString(QUERY,"popular");
            movieListAdapter = new GridAdapter(getMovies(1), mtwoPane, getFragmentManager(), realm);
        }

        tabBar.selectTab(tabBar.getTabAt(getSelectedTabIndex()));
        tabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                handleSelectedTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Initializing the Grid Recycler View
        movieLayoutManager= new GridLayoutManager(getApplicationContext(),NUM_COLS);
        movieGridView.setLayoutManager(movieLayoutManager);
        movieGridView.setAdapter(movieListAdapter);
        movieGridView.setItemViewCacheSize(32);

        //Setup swipe to refresh
        refreshLayout.setOnRefreshListener(() -> {
            //Loads the initial contents
            refreshLayout.setRefreshing(true);
            movieListAdapter.clear();
            movieListAdapter.add(getMovies(1));
            movieListAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        });

        //Setup pagination
        Paginate.with(movieGridView, this)
                .addLoadingListItem(true)
                .setLoadingListItemSpanSizeLookup(() -> NUM_COLS)
                .build();

        //Setup default second pane
//        if (mtwoPane) {
//            MovieDetailFragment detailFragment = new MovieDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("data", movieList.get(0));
//            detailFragment.setArguments(bundle);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.container, detailFragment)
//                    .commit();
//        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIELIST, movieList);
        outState.putString(QUERY,query);
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

    @Override
    public synchronized void onLoadMore() {
        loading = true;
        movieListAdapter.add(getMovies(currentPgNo + 1));
        loading = false;
    }

    @Override
    public synchronized boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return currentPgNo==totalPgNo;
    }

    @Nullable
    private List<Parcelable> getMovies(int curpg) {
        List<Parcelable> tempMovieList = null;
        if (query.contains("favourites") && curpg == 1) {
            tempMovieList = (List) realm.where(MovieFullData.class).findAll();
            return tempMovieList;
        } else if (query.contains("favourites"))
            return null;
        MovieFetcher fetch = new MovieFetcher();
        fetch.execute(query, Integer.toString(curpg));
        try {
            MovieHeader tempMovie = fetch.get();
            currentPgNo = tempMovie.getCurrent();
            totalPgNo = tempMovie.getTotal();
            tempMovieList = (List) tempMovie.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tempMovieList == null)
            Snackbar.make(container, "Please check your network connection or view Favourites!!!",
                    Snackbar.LENGTH_LONG).show();
        else
            movieList.addAll(tempMovieList);
        return tempMovieList;
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

    private void handleSelectedTab(int position) {
        if (position == 0) {
            query = "popular";
        } else if (position == 1) {
            query = "top_rated";
        } else if (position == 2) {
            query = "favourites";
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(QUERY, query);
        editor.apply();

        movieListAdapter.clear();
        movieListAdapter.add(getMovies(1));
        movieListAdapter.notifyDataSetChanged();
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
}
