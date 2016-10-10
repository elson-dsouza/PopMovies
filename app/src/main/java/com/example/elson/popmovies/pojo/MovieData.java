package com.example.elson.popmovies.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieData implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    @SerializedName("id") Long id;
    @SerializedName("poster_path") String poster;
    @SerializedName("vote_average")
    Double rating;
    @SerializedName("title")
    String title;

    protected MovieData(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        poster = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        title = in.readString();
    }

    public String getPoster() {
        return poster;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return Double.toString(rating) + "/10";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
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