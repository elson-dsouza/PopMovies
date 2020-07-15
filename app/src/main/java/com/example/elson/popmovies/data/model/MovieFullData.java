package com.example.elson.popmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Elson on 10-10-2016.
 */
public class MovieFullData extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("runtime")
    int duration;

    @SerializedName("release_date")
    String release;

    @SerializedName("vote_average")
    double rating;

    @SerializedName("overview")
    String description;

    @SerializedName("poster_path")
    String poster;

    @SerializedName("adult")
    boolean isAdult;

    @SerializedName("backdrop_path")
    String backdrop;

    public MovieFullData() {
    }

    protected MovieFullData(@NonNull Parcel in) {
        id = in.readInt();
        title = in.readString();
        duration = in.readInt();
        release = in.readString();
        rating = in.readDouble();
        description = in.readString();
        poster = in.readString();
        isAdult = in.readByte() != 0x00;
        backdrop = in.readString();
    }

    public static final Creator<MovieFullData> CREATOR = new Creator<MovieFullData>() {
        @Override
        public MovieFullData createFromParcel(Parcel in) {
            return new MovieFullData(in);
        }

        @Override
        public MovieFullData[] newArray(int size) {
            return new MovieFullData[size];
        }
    };

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDuration() {
        return Integer.toString(duration) + "min";
    }

    @NonNull
    public String getRating() {
        return Double.toString(rating) + "/10";
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getPoster() {
        return poster;
    }

    public int getId() {
        return id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(release);
        dest.writeDouble(rating);
        dest.writeString(description);
        dest.writeString(poster);
        dest.writeByte((byte) (isAdult ? 0x01 : 0x00));
        dest.writeString(backdrop);
    }
}
