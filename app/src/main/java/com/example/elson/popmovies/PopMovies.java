package com.example.elson.popmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elson.popmovies.pojo.MovieHeader;
import com.facebook.stetho.Stetho;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopMovies extends AppCompatActivity implements Paginate.Callbacks {

    //Views injected using ButterKnife
    @BindView(R.id.movieGrid)
    RecyclerView movieGridView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private static final String MOVIELIST ="MOVIEKEY" ;
    private static final String QUERY = "QUERYKEY";
    private List<com.example.elson.popmovies.pojo.MovieData> movieList;
    private GridAdapter movieListAdapter;
    private GridLayoutManager movieLayoutManager;
    private String query="popular";
    private MovieHeader movies;
    private boolean mtwoPane;
    private int currentPgNo=1;
    private int totalPgNo=1;
    private boolean loading = false;

    private final int NUM_COLS = 2;

    public PopMovies() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        query=preferences.getString(QUERY,"popular");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_movies);
        movieList=new ArrayList<>();

        //Initialize butterknife and stetho
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        //Know one pane or two pane UI is loaded
        mtwoPane = findViewById(R.id.container) != null;

        //Initializing the Grid Recycler View
        movieLayoutManager= new GridLayoutManager(getApplicationContext(),NUM_COLS);
        movieGridView.setLayoutManager(movieLayoutManager);


       //Loads the initial contents
        MovieFetcher fetch=new MovieFetcher();
        fetch.execute(query,Integer.toString(currentPgNo));
        try {
            MovieHeader tempMovie=fetch.get();
            currentPgNo=tempMovie.getCurrent();
            totalPgNo=tempMovie.getTotal();
            movieList=tempMovie.getResult();
            movieListAdapter=new GridAdapter(movieList);
            movieGridView.setAdapter(movieListAdapter);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Setup swipe to refresh
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Loads the initial contents
                refreshLayout.setRefreshing(true);
                MovieFetcher fetch=new MovieFetcher();
                fetch.execute(query,Integer.toString(currentPgNo));
                try {
                    MovieHeader tempMovie=fetch.get();
                    currentPgNo=tempMovie.getCurrent();
                    totalPgNo=tempMovie.getTotal();
                    movieList=tempMovie.getResult();
                    movieListAdapter.clear();
                    movieListAdapter.add(movieList);
                    movieListAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        //Setup pagination
        Paginate.with(movieGridView, this)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator())
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return NUM_COLS;
                    }
                })
                .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_movies, menu);

        if(query.contains("popular"))
            menu.findItem(R.id.action_popular).setChecked(true);
        else if(query.contains("top_rated"))
            menu.findItem(R.id.action_rating).setChecked(true);
        else
            menu.findItem(R.id.action_favourites).setChecked(true);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(MOVIELIST,movies);
        outState.putString(QUERY,query);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies=savedInstanceState.getParcelable(MOVIELIST);
        query=savedInstanceState.getString(QUERY);
        //movieListAdapter= new GridAdapter(movies.getResult());
        //movieGridView.setAdapter(movieListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        }
        super.onOptionsItemSelected(item);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(QUERY,query);
        editor.apply();
        movieListAdapter.clear();
        movieListAdapter.notifyDataSetChanged();

        MovieFetcher fetch=new MovieFetcher();
        fetch.execute(query,Integer.toString(1));
        try {
            MovieHeader tempMovie=fetch.get();
            currentPgNo=tempMovie.getCurrent();
            totalPgNo=tempMovie.getTotal();
            movieList=tempMovie.getResult();
            movieListAdapter.add(movieList);
            movieListAdapter.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public synchronized void onLoadMore() {
        Log.d("Paginate", "onLoadMore");
        loading = true;
        MovieFetcher fetch=new MovieFetcher();
        fetch.execute(query,Integer.toString(currentPgNo+1));
        try {
            MovieHeader tempMovie=fetch.get();
            currentPgNo=tempMovie.getCurrent();
            totalPgNo=tempMovie.getTotal();
            movieList=tempMovie.getResult();
            movieListAdapter.add(movieList);
        } catch (Exception e){
            e.printStackTrace();
        }
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

    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // Bind custom loading row if needed
            VH vh = (VH) holder;

        }
    }
    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;
        ProgressBar imgLoading;

        public VH(View itemView) {
            super(itemView);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
            imgLoading= (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

}
