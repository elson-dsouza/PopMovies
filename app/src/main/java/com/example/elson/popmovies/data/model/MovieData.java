package com.example.elson.popmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class MovieData implements Parcelable {

    @Nullable
    @SerializedName("id")
    Integer id;
    @SerializedName("poster_path") String poster;
    @Nullable
    @SerializedName("vote_average")
    Double rating;
    @SerializedName("title")
    String title;

    MovieData(@NonNull Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        poster = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        title = in.readString();
    }

    public MovieData(@NonNull MovieFullData data) {
        this.id = data.getId();
        this.poster = data.getPoster();
        this.title = data.getTitle();
        this.rating = data.getRawRating();
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

    public String getPoster() {
        return poster;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public String getRating() {
        return Double.toString(rating) + "/10";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(poster);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(rating);
        }
        dest.writeString(title);
    }
}