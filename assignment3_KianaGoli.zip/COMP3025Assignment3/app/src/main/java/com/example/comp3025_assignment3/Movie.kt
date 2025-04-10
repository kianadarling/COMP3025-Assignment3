package com.example.comp3025_assignment3

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: String,
    val title: String,
    val studio: String,
    val thumbnail: Int,
    val criticsRating: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(studio)
        parcel.writeInt(thumbnail)
        parcel.writeDouble(criticsRating)
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
