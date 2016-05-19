package com.example.elson.popmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieDetails extends AppCompatActivity {

    private static final String QUERY = "Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras=getIntent().getExtras();
        String movie[]=extras.getString(QUERY).split(" ");
        ImageView poster = (ImageView) findViewById(R.id.poster);
        Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+movie[1]).into(poster);
        FetchMovies fetchMovies=new FetchMovies();
        fetchMovies.execute(movie[0]);
        try {
            JSONObject json = new JSONObject(fetchMovies.get());
            TextView textView=(TextView)findViewById(R.id.title);
            textView.setText(json.getString("original_title"));
            textView=(TextView)findViewById(R.id.duration);
            textView.setText(Integer.toString(json.getInt("runtime"))+"min");
            textView=(TextView)findViewById(R.id.year);
            String string=json.getString("release_date");
            textView.setText(string.split("-")[0]);
            textView=(TextView)findViewById(R.id.rating);
            textView.setText(Double.toString(json.getDouble("vote_average"))+"/10");
            textView=(TextView)findViewById(R.id.plot);
            textView.setText(json.getString("overview"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
