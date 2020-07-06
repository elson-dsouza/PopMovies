package com.example.elson.popmovies.ui.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.elson.popmovies.R;
import com.example.elson.popmovies.async.MovieFetcher;
import com.example.elson.popmovies.data.model.MovieFullData;
import com.example.elson.popmovies.data.model.MovieHeader;
import com.google.android.material.snackbar.Snackbar;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class PopMovies extends AppCompatActivity implements Paginate.Callbacks {

    private static final String MOVIELIST = "MOVIEKEY";
    private static final String QUERY = "QUERYKEY";
    private final int NUM_COLS = 2;

    @BindView(R.id.movieGrid)
    RecyclerView movieGridView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.container)
    FrameLayout container;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_movies);
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this);
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_movies, menu);

        if(query.contains("popular"))
            menu.findItem(R.id.action_popular).setChecked(true);
        else if(query.contains("top_rated"))
            menu.findItem(R.id.action_rating).setChecked(true);
        else if (query.contains("favourites"))
            menu.findItem(R.id.action_favourites).setChecked(true);
        return true;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.action_rating) {
            query="top_rated";
        } else if(id == R.id.action_popular) {
            query="popular";

        } else if (id == R.id.action_favourites) {
            query = "favourites";
        }
        super.onOptionsItemSelected(item);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(QUERY, query);
        editor.apply();

        movieListAdapter.clear();
        movieListAdapter.add(getMovies(1));
        movieListAdapter.notifyDataSetChanged();

        return true;
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
}
