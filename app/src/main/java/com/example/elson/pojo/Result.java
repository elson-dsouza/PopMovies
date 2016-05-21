package com.example.elson.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elson on 21-05-2016.
 */
public class Result implements Parcelable {
    @SerializedName("poster_path") String poster;
    @SerializedName("id") Long id;

    public String getPoster(){
        return poster;
    }

    public Long getId(){
        return id;
    }

    protected Result(Parcel in) {
        poster = in.readString();
        id = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeLong(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}