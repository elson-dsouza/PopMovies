package com.example.elson.popmovies.data.model

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    val id: Long,
    val poster: String?,
    val rating: Double,
    val title: String?,
    @IgnoredOnParcel
    val isFavorite: LiveData<Boolean> = MutableLiveData(false)
) : Parcelable {
    val ratingString: String get() = "$rating/10"
}
