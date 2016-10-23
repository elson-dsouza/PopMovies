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
    @SerializedName("id")
    Integer id;
    @SerializedName("poster_path") String poster;
    @SerializedName("vote_average")
    Double rating;
    @SerializedName("title")
    String title;

    MovieData(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        poster = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        title = in.readString();
    }

    public MovieData(MovieFullData data) {
        this.id = data.getId();
        this.poster = data.getPoster();
        this.title = data.getTitle();
        this.rating = data.getRawRating();
    }

    public String getPoster() {
        return poster;
    }

    public int getId() {
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