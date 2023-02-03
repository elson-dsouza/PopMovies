package com.example.elson.popmovies.data.enumeration

import androidx.annotation.StringRes
import com.example.elson.popmovies.R

enum class MovieTypes(val queryPath: String, @StringRes val nameRes: Int) {
    POPULAR("popular", R.string.tab_popular),
    NOW_PLAYING("now_playing", R.string.tab_now_playing),
    UPCOMING("upcoming", R.string.tab_upcoming),
    TOP_RATED("top_rated", R.string.tab_top_rated)
}
