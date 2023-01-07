package com.example.elson.popmovies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Genre : RealmObject, Parcelable {

    @IgnoredOnParcel
    @SerializedName("name")
    var name: String = ""

    override fun describeContents() = 0
}
