package com.example.elson.popmovies.data.model

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

const val SOURCE_YOUTUBE = "youtube"
const val YOUTUBE_PREFIX = "https://www.youtube.com/embed/"

const val LOG_TAG = "VideoModel"

@Parcelize
open class Video @JvmOverloads constructor (
        @SerializedName("name") open var name: String = "",
        @SerializedName("key") open var key: String = "",
        @SerializedName("site") open var site: String = ""
) : RealmObject(), Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    val url: String
        get() =
            if (site.equals(SOURCE_YOUTUBE, ignoreCase = true)) {
                "$YOUTUBE_PREFIX$key"
            } else {
                Log.e(LOG_TAG, "Unknown source $site")
                ""
            }
}