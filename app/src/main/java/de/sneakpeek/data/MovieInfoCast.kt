package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfoCast(@Expose @SerializedName("character") var character: String?,
                         @Expose @SerializedName("name") var name: String?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfoCast> = object : Parcelable.Creator<MovieInfoCast> {
            override fun createFromParcel(source: Parcel): MovieInfoCast = MovieInfoCast(source)
            override fun newArray(size: Int): Array<MovieInfoCast?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(character)
        dest.writeString(name)
    }
}


