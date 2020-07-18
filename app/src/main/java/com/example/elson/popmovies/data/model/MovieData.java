package com.example.elson.popmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MovieData extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    long id;

    @SerializedName("poster_path")
    String poster;

    @SerializedName("vote_average")
    double rating;

    @SerializedName("title")
    String title;

    public MovieData() {
    }

    public MovieData(@NonNull Parcel in) {
        id = in.readLong();
        poster = in.readString();
        rating = in.readDouble();
        title = in.readString();
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @NonNull
        @Override
        public MovieData createFromParcel(@NonNull Parcel in) {
            return new MovieData(in);
        }

        @NonNull
        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Nullable
    public String getPoster() {
        return poster;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getRating() {
        return rating + "/10";
    }

    public boolean isFavorite() {
        try (Realm db = Realm.getDefaultInstance()){
            return db.where(MovieData.class).equalTo("id", id).findFirst() != null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(poster);
        dest.writeDouble(rating);
        dest.writeString(title);
    }
}