package com.example.elson.popmovies.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elson on 21-05-2016.
 */
public class MovieHeader implements Parcelable {

    @SerializedName("page") int page;
    @SerializedName("results") List<MovieData> result;
    @SerializedName("total_pages") int total;

    public List<MovieData> getResult(){
        return result;
    }

    protected MovieHeader(Parcel in) {
        page = in.readInt();
        if (in.readByte() == 0x01) {
            result = new ArrayList<>();
            in.readList(result, MovieData.class.getClassLoader());
        } else {
            result = null;
        }
        total = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        if (result == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(result);
        }
        dest.writeInt(total);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieHeader> CREATOR = new Parcelable.Creator<MovieHeader>() {
        @Override
        public MovieHeader createFromParcel(Parcel in) {
            return new MovieHeader(in);
        }

        @Override
        public MovieHeader[] newArray(int size) {
            return new MovieHeader[size];
        }
    };
}