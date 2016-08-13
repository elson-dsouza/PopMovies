package com.example.elson.popmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.elson.popmovies.pojo.MovieHeader;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopMovies extends AppCompatActivity {

    private final String API_KEY=;//Add your API key here

    //Views injected using ButterKnife
    @BindView(R.id.movieGrid) private RecyclerView movieGridView;

    private static final String MOVIELIST ="MOVIEKEY" ;
    private static final String QUERY = "QUERYKEY";
    private ArrayList<com.example.elson.popmovies.pojo.MovieData> movieList;
    private RecyclerView.Adapter movieListAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private String query;
    private MovieHeader movies;
    private Retrofit retrofit;
    private boolean mtwoPane;
    private int numRows = 2;

    public PopMovies() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        query=preferences.getString(QUERY,"popular");

        setContentView(R.layout.activity_pop_movies);

        //Know one pane or two pane UI is loaded
        mtwoPane = findViewById(R.id.container) != null;

        //Initializing the grid Recycler View
        movieGridView.setHasFixedSize(true);
        movieLayoutManager= new GridLayoutManager(getApplicationContext(),numRows);
        movieGridView.setLayoutManager(movieLayoutManager);

        //Data fetched and populated using Retrofit and GSON
        retrofit=new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FetchData fetch=retrofit.create(FetchData.class);
        Call<MovieHeader> call=fetch.getMovies(query,API_KEY);
        call.enqueue(new Callback<MovieHeader>() {
            @Override
            public void onResponse(Call<MovieHeader> call, Response<MovieHeader> response) {
                movies=response.body();
                movieListAdapter= new GridAdapter(movies.getResult());
                movieGridView.setAdapter(movieListAdapter);

//              movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if(mtwoPane) {
//                            FragmentManager fragmentManager=getFragmentManager();
//                            fragmentManager.beginTransaction().replace(R.id.container,new MovieDetailFragment()).commit();
//                        }
//                        else {
//                            Intent movieDetailIntent = new Intent(getApplication(), MovieDetail.class).putExtra("Id", movies.getResult().get(position));
//                            startActivity(movieDetailIntent);
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(Call<MovieHeader> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Please check network and try again",Toast.LENGTH_SHORT).show();
            }
        });
        Stetho.initializeWithDefaults(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_movies, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIELIST,movies);
        outState.putString(QUERY,query);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies=savedInstanceState.getParcelable(MOVIELIST);
        query=savedInstanceState.getString(QUERY);
        movieListAdapter= new GridAdapter(movies.getResult());
        movieGridView.setAdapter(movieListAdapter);

//        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(mtwoPane) {
//                    FragmentManager fragmentManager=getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.container,new MovieDetailFragment()).commit();
//                }
//                else {
//                    Intent movieDetailIntent = new Intent(getApplication(), MovieDetail.class).putExtra("Id", movies.getResult().get(position));
//                    startActivity(movieDetailIntent);
//                }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rating) {
            query="top_rated";
        }
        else if(id == R.id.action_popular) {
            query="popular";

        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(QUERY,query);
        editor.apply();
        retrofit=new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FetchData fetch=retrofit.create(FetchData.class);
        Call<MovieHeader> call=fetch.getMovies(query,API_KEY);
        call.enqueue(new Callback<MovieHeader>() {
            @Override
            public void onResponse(Call<MovieHeader> call, Response<MovieHeader> response) {
                movies=response.body();
                movieListAdapter= new GridAdapter(movies.getResult());
                movieGridView.setAdapter(movieListAdapter);

//                movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if(mtwoPane) {
//                            FragmentManager fragmentManager=getFragmentManager();
//                            fragmentManager.beginTransaction().replace(R.id.container,new MovieDetailFragment()).commit();
//                        }
//                        else {
//                            Intent movieDetailIntent = new Intent(getApplication(), MovieDetail.class).putExtra("Id", movies.getResult().get(position));
//                            startActivity(movieDetailIntent);
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(Call<MovieHeader> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Please check network and try again",Toast.LENGTH_SHORT).show();
            }
        });
        return super.onOptionsItemSelected(item);
    }

}
