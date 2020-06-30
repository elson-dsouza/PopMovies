package com.example.elson.popmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.elson.popmovies.Adapters.GridAdapter;
import com.example.elson.popmovies.Asyncs.MovieFetcher;
import com.example.elson.popmovies.pojo.MovieFullData;
import com.example.elson.popmovies.pojo.MovieHeader;
import com.facebook.stetho.Stetho;
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
    //Views injected using ButterKnife
    @Nullable
    @BindView(R.id.movieGrid)
    RecyclerView movieGridView;
    @Nullable
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @BindView(R.id.container)
    FrameLayout container;
    @Nullable
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        query = preferences.getString(QUERY,"popular");
        realm = Realm.getDefaultInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_movies);
        movieList=new ArrayList<>();

        //Initialize butterknife, stetho and 2 pane UI
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        mtwoPane = container != null;

        //Initializing the Grid Recycler View
        movieLayoutManager= new GridLayoutManager(getApplicationContext(),NUM_COLS);
        movieGridView.setLayoutManager(movieLayoutManager);
        //movieGridView.setItemViewCacheSize(10);

       //Loads the initial contents
        movieListAdapter = new GridAdapter(getMovies(1), mtwoPane, getFragmentManager(), realm);
        movieGridView.setAdapter(movieListAdapter);

        //Setup swipe to refresh
        refreshLayout.setOnRefreshListener(() -> {
            //Loads the initial contents
            refreshLayout.setRefreshing(true);
            movieListAdapter.clear();
            movieListAdapter.add(getMovies(1));
            movieListAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        });
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieList = savedInstanceState.getParcelableArrayList(MOVIELIST);
        query=savedInstanceState.getString(QUERY);
        movieListAdapter = new GridAdapter(movieList, mtwoPane, getFragmentManager(), realm);
        movieGridView.setAdapter(movieListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        item.setChecked(true);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rating) {
            query="top_rated";
        }
        else if(id == R.id.action_popular) {
            query="popular";

        } else if (id == R.id.action_favourites) {
            query = "favourites";
        }
        super.onOptionsItemSelected(item);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(QUERY,query);
        editor.apply();
        movieListAdapter.clear();
        movieListAdapter.notifyDataSetChanged();
        movieListAdapter.add(getMovies(1));
        movieListAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public synchronized void onLoadMore() {
        loading = true;
        //loadingItem.setVisibility(true);
        movieListAdapter.add(getMovies(currentPgNo + 1));
        //loadingItem.setVisibility(false);
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
            Toast.makeText(this,
                    "Please check your network connection or view Favourites!!!",
                    Toast.LENGTH_SHORT).show();
        else
            movieList.addAll(tempMovieList);
        return tempMovieList;
    }
}
