package com.example.elson.popmovies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Genre @JvmOverloads constructor (
        @SerializedName("name") open var name: String = ""
) : RealmObject(), Parcelable {

    override fun describeContents(): Int {
        return 0
    }

}