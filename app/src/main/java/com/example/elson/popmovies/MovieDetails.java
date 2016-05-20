package com.example.elson.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    private static final String QUERY = "Id";

    @BindView(R.id.title) TextView title;
    @BindView(R.id.duration) TextView duration;
    @BindView(R.id.year) TextView year;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.plot) TextView plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        String movie[] = extras.getString(QUERY).split(" ");
        ImageView poster = (ImageView) findViewById(R.id.poster);
        Glide.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185/" + movie[1])
                .into(poster);
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute(movie[0]);
        try {
            JSONObject json = new JSONObject(fetchMovies.get());
            title.setText(json.getString("original_title"));
            duration.setText(Integer.toString(json.getInt("runtime")) + "min");
            String string = json.getString("release_date");
            year.setText(string.split("-")[0]);
            rating.setText(Double.toString(json.getDouble("vote_average")) + "/10");
            plot.setText(json.getString("overview"));

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
