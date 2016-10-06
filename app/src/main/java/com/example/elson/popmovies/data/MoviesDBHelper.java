package com.example.elson.popmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.elson.popmovies.pojo.MovieData;

public class MoviesDBHelper extends SQLiteOpenHelper {

    //name and version
    public static final String DATABASE_NAME="fav_movies.db";
    public static final int DATABASE_VERSION=1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_FAVORITES + "(" +
                MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                MoviesContract.MovieEntry.COLUMN_ID + " UNSIGNED BIG INT NOT NULL , " +
                MoviesContract.MovieEntry.COLUMN_TITLE+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_ISADULT+" BOOLEAN , "+
                MoviesContract.MovieEntry.COLUMN_RATING+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_DESCRIPTION+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_BACKGROUND_PATH+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_DURATION+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_POPULARITY+" TEXT , "+
                MoviesContract.MovieEntry.COLUMN_VIDEO+" BOOLEAN , "+
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(MoviesDBHelper.DATABASE_NAME,"Upgrading database");
        db.execSQL("DROP TABLE IF EXISTS "+MoviesContract.MovieEntry.TABLE_FAVORITES);
        onCreate(db);
    }

    public static ContentValues getMovieContentValues(MovieData movieData){
        ContentValues contentValues=new ContentValues();
        contentValues.put(MoviesContract.MovieEntry._ID,movieData.getId());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_TITLE,movieData.getTitle());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH,movieData.getPoster());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_ISADULT,movieData.getIsAdult());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_RATING,movieData.getRating());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,movieData.getRelease());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_DESCRIPTION,movieData.getDescription());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_BACKGROUND_PATH,movieData.getBackdrop());
        contentValues.put(MoviesContract.MovieEntry.COLUMN_DURATION,movieData.getDuration());
        return contentValues;
    }
}
