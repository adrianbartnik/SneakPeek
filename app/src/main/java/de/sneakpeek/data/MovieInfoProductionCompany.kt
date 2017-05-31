package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfoProductionCompany(
        @Expose @SerializedName("name") var name: String?,
        @Expose @SerializedName("id") var id: Int = 0) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfoProductionCompany> = object : Parcelable.Creator<MovieInfoProductionCompany> {
            override fun createFromParcel(source: Parcel): MovieInfoProductionCompany = MovieInfoProductionCompany(source)
            override fun newArray(size: Int): Array<MovieInfoProductionCompany?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(id)
    }
}