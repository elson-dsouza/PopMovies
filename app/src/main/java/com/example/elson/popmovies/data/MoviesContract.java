package com.example.elson.popmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String CONTENT_AUTHORITY="com.example.elson.popmovies";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY );

    public static final class MovieEntry implements BaseColumns{

        // table name
        public static final String TABLE_FAVORITES = "favourites";

        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_ID="movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH="poster";
        public static final String COLUMN_ISADULT="adult";
        public static final String COLUMN_RATING="rating";
        public static final String COLUMN_RELEASE_DATE="release";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_BACKGROUND_PATH="background";
        public static final String COLUMN_DURATION="duration";
        public static final String COLUMN_VIDEO = "video";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITES).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;

        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;

        // for building URIs on insertion
        public static Uri buildFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
