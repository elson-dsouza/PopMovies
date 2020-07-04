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
    @Nullable
    @SerializedName("vote_average")
    Double rating;
    @SerializedName("overview")
    String description;
    @SerializedName("poster_path")
    String poster;
    @Nullable
    @SerializedName("adult")
    Boolean isAdult;
    @SerializedName("backdrop_path")
    String backdrop;

    protected MovieFullData(@NonNull Parcel in) {
        id = in.readInt();
        title = in.readString();
        duration = in.readInt();
        release = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        description = in.readString();
        poster = in.readString();
        byte isAdultVal = in.readByte();
        isAdult = isAdultVal == 0x02 ? null : isAdultVal != 0x00;
        backdrop = in.readString();
    }

    public MovieFullData() {
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

    public String getDescription() {
        return description;
    }

    public String getPoster() {
        return poster;
    }

    public int getId() {
        return id;
    }

    public String getRelease() {
        return release;
    }

    @Nullable
    public Boolean getIsAdult() {
        return isAdult;
    }

    public String getBackdrop() {
        return backdrop;
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
    }

    @Nullable
    public Double getRawRating() {
        return rating;
    }
}
