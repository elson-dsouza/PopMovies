package com.example.elson.popmovies.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val SOURCE_YOUTUBE = "youtube"
const val YOUTUBE_PREFIX = "https://www.youtube.com/embed/"

@Parcelize
data class VideoModel(
    val name: String,
    val key: String,
    val site: String
) : Parcelable {

    val url: String get() = when {
        site.equals(SOURCE_YOUTUBE, ignoreCase = true) -> "$YOUTUBE_PREFIX$key"
        else -> throw IllegalArgumentException("Unknown video source $site")
    }
}
