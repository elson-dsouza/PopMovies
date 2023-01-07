package com.example.elson.popmovies.data.enumeration

enum class MovieTypes(val queryPath: String) {
    POPULAR("popular"),
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    TOP_RATED("top_rated")
}
