package com.example.elson.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PopMovies extends AppCompatActivity {

    private static final String MOVIELIST ="MOVIEKEY" ;
    private static final String QUERY = "QUERYKEY";
    private GridView movieGridView;
    private ArrayList<String> movieList;
    private ArrayAdapter<String>movieListAdapter;
    private String query;

    public PopMovies() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        query=preferences.getString(QUERY,"popular");
        setContentView(R.layout.activity_pop_movies);
        FetchMovies fetchMovies=new FetchMovies();
        try {
            fetchMovies.execute(query);
            String json=fetchMovies.get();
            movieList=getDataFromJson(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        movieListAdapter= new GridAdapter(getApplicationContext(),movieList);
        movieGridView=(GridView)findViewById(R.id.movieGrid);
        movieGridView.setAdapter(movieListAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieDetailIntent=new Intent(getApplication(),MovieDetails.class).putExtra("Id",movieList.get(position));
                startActivity(movieDetailIntent);
            }
        });
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
        outState.putStringArrayList(MOVIELIST,movieList);
        outState.putString(QUERY,query);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieList=savedInstanceState.getStringArrayList(MOVIELIST);
        query=savedInstanceState.getString(QUERY);
        movieListAdapter= new GridAdapter(getApplicationContext(),movieList);
        movieGridView=(GridView)findViewById(R.id.movieGrid);
        movieGridView.setAdapter(movieListAdapter);
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent movieDetailIntent=new Intent(getApplication(),MovieDetails.class).putExtra("Id",movieList.get(position));
                startActivity(movieDetailIntent);
            }
        });
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
        try {
            FetchMovies fetchMovies=new FetchMovies();
            fetchMovies.execute(query);
            String json=fetchMovies.get();
            movieList=getDataFromJson(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieListAdapter= new GridAdapter(getApplicationContext(),movieList);
        movieGridView=(GridView)findViewById(R.id.movieGrid);
        movieGridView.setAdapter(movieListAdapter);

        return super.onOptionsItemSelected(item);
    }

    private class GridAdapter extends ArrayAdapter<String> {
        public GridAdapter(Context applicationContext, List<String> movieList) {
            super(applicationContext,0, movieList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String poster=getItem(position);
            String t[]=poster.split(" ");
            poster=t[1];
            if(convertView==null){
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.movies,parent,false);
            }
            ImageView iconView = (ImageView) convertView.findViewById(R.id.movieView);
            Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+poster).into(iconView);
            return convertView;
        }
    }

    private ArrayList<String> getDataFromJson(String jsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        ArrayList<String> result = new ArrayList<>();

        JSONObject json = new JSONObject(jsonStr);
        JSONArray jsonArray=json.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject t=jsonArray.getJSONObject(i);
            long j=t.getLong("id");
            String k=t.getString("poster_path");
            result.add(Long.toString(j)+" "+k);
        }
        return result;
    }
}
