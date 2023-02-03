package com.example.elson.popmovies.ui

import androidx.annotation.StringRes
import com.example.elson.popmovies.R

enum class ActivityDestinations(@StringRes val label: Int) {
    MOVIES(label = R.string.title_activity_movies),
    LOGIN(label = R.string.title_activity_login)
}
