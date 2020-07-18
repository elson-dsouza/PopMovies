package com.example.elson.popmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Elson on 10-10-2016.
 */
public class FullMovieData extends RealmObject implements Parcelable {

    @SerializedName("tagline")
    String tagline;

    @SerializedName("runtime")
    int duration;

    @SerializedName("release_date")
    String release;

    @SerializedName("overview")
    String description;

    @SerializedName("adult")
    boolean isAdult;

    @SerializedName("backdrop_path")
    String backdrop;

    @PrimaryKey
    @SerializedName("id")
    long id;

    @SerializedName("poster_path")
    String poster;

    @SerializedName("vote_average")
    double rating;

    @SerializedName("title")
    String title;

    public FullMovieData() {
    }

    protected FullMovieData(@NonNull Parcel in) {
        id = in.readLong();
        title = in.readString();
        duration = in.readInt();
        release = in.readString();
        rating = in.readDouble();
        description = in.readString();
        poster = in.readString();
        isAdult = in.readByte() != 0x00;
        backdrop = in.readString();
        tagline = in.readString();
    }

    public static final Creator<FullMovieData> CREATOR = new Creator<FullMovieData>() {
        @Override
        public FullMovieData createFromParcel(Parcel in) {
            return new FullMovieData(in);
        }

        @Override
        public FullMovieData[] newArray(int size) {
            return new FullMovieData[size];
        }
    };

    @NonNull
    public String getDuration() {
        return duration + " min";
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getRelease() {
        return release;
    }

    public boolean getIsAdult() {
        return isAdult;
    }

    @Nullable
    public String getBackdrop() {
        return backdrop;
    }

    public double getRawRating() {
        return rating;
    }

    @Nullable
    public String getTagline() {
        return tagline;
    }

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
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(release);
        dest.writeDouble(rating);
        dest.writeString(description);
        dest.writeString(poster);
        dest.writeByte((byte) (isAdult ? 0x01 : 0x00));
        dest.writeString(backdrop);
        dest.writeString(tagline);
    }
}
