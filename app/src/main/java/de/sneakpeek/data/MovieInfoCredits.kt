package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfoCredits(
        @Expose @SerializedName("cast") var cast: List<MovieInfoCast>?,
        @Expose @SerializedName("crew") var crew: List<MovieInfoCrew>?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfoCredits> = object : Parcelable.Creator<MovieInfoCredits> {
            override fun createFromParcel(source: Parcel): MovieInfoCredits = MovieInfoCredits(source)
            override fun newArray(size: Int): Array<MovieInfoCredits?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.createTypedArrayList(MovieInfoCast.CREATOR),
            source.createTypedArrayList(MovieInfoCrew.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(cast)
        dest.writeTypedList(crew)
    }
}


