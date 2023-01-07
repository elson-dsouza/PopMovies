package com.example.elson.popmovies.data.model

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("poster_path")
    var poster: String? = null,

    @SerializedName("vote_average")
    var rating: Double = 0.0,

    @SerializedName("title")
    var title: String? = null
) : Parcelable {

    @IgnoredOnParcel
    var isFavorite: LiveData<Boolean> = MutableLiveData(false)

    fun getRatingString(): String {
        return "$rating/10"
    }
}
