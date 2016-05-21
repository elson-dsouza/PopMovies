package com.example.elson.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.elson.pojo.MovieData;
import com.example.elson.pojo.MovieHeader;
import com.example.elson.pojo.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    private static final String QUERY = "Id";
    private final String API_KEY;//Add your API key here
    private MovieData data;


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
        Result temp=extras.getParcelable(QUERY);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FetchData fetchData=retrofit.create(FetchData.class);
        Call<MovieData> call=fetchData.getData(temp.getId(),API_KEY);
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                data=response.body();
                title.setText(data.getTitle());
                duration.setText(data.getDuration());
                year.setText(data.getYear());
                rating.setText(data.getRating());
                plot.setText(data.getPlot());
                ImageView poster = (ImageView) findViewById(R.id.poster);
                Glide.with(getApplicationContext())
                        .load("http://image.tmdb.org/t/p/w185/" + data.getPoster())
                        .into(poster);

            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Please check network and try again",Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
