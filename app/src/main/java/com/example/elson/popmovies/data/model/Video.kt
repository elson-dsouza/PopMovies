package com.example.elson.popmovies.data.model

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

const val SOURCE_YOUTUBE = "youtube"
const val YOUTUBE_PREFIX = "https://www.youtube.com/embed/"

const val LOG_TAG = "VideoModel"

@Parcelize
open class Video : RealmObject, Parcelable {

    @IgnoredOnParcel
    @SerializedName("name")
    var name: String = ""

    @IgnoredOnParcel
    @SerializedName("key")
    var key: String = ""

    @IgnoredOnParcel
    @SerializedName("site")
    var site: String = ""

    override fun describeContents() = 0

    val url: String
        get() =
            if (site.equals(SOURCE_YOUTUBE, ignoreCase = true)) {
                "$YOUTUBE_PREFIX$key"
            } else {
                Log.e(LOG_TAG, "Unknown source $site")
                ""
            }
}
