package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfoCrew(
        @Expose @SerializedName("name") var name: String?,
        @Expose @SerializedName("job") var job: String?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfoCrew> = object : Parcelable.Creator<MovieInfoCrew> {
            override fun createFromParcel(source: Parcel): MovieInfoCrew = MovieInfoCrew(source)
            override fun newArray(size: Int): Array<MovieInfoCrew?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(job)
    }
}


