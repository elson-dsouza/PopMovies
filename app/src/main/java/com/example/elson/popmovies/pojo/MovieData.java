package com.example.elson.popmovies.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieData implements Parcelable {

    @SerializedName("id") Long id;
    @SerializedName("title") String title;
    @SerializedName("runtime") int duration;
    @SerializedName("release_date")String release;
    @SerializedName("vote_average") Double rating;
    @SerializedName("overview") String description;
    @SerializedName("poster_path") String poster;
    @SerializedName("adult") Boolean isAdult;
    @SerializedName("backdrop_path") String backdrop;
    @SerializedName("popularity") private double popularity;
    @SerializedName("video") private boolean video;

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return Integer.toString(duration)+"min";
    }

    public String getRating() {
        return Double.toString(rating)+"/10";
    }

    public String getDescription() {
        return description;
    }

    public String getPoster() {
        return poster;
    }

    public Long getId() {
        return id;
    }

    public String getRelease() {
        return release;
    }

    public Boolean getIsAdult() {
        return isAdult;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public boolean getIsVideo() {
        return video;
    }

    public String getPopularity() {
        return Double.toString(popularity);
    }


    protected MovieData(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        title = in.readString();
        duration = in.readInt();
        release = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        description = in.readString();
        poster = in.readString();
        byte isAdultVal = in.readByte();
        isAdult = isAdultVal == 0x02 ? null : isAdultVal != 0x00;
        backdrop = in.readString();
        popularity = in.readDouble();
        video = in.readByte() != 0x00;
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
        dest.writeString(title);
        dest.writeInt(duration);
        dest.writeString(release);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(rating);
        }
        dest.writeString(description);
        dest.writeString(poster);
        if (isAdult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isAdult ? 0x01 : 0x00));
        }
        dest.writeString(backdrop);
        dest.writeDouble(popularity);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
    }

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
}