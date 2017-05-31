package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfoGenre(@Expose @SerializedName("id") var id: Int = 0,
                          @Expose @SerializedName("name") var name: String?) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfoGenre> = object : Parcelable.Creator<MovieInfoGenre> {
            override fun createFromParcel(source: Parcel): MovieInfoGenre = MovieInfoGenre(source)
            override fun newArray(size: Int): Array<MovieInfoGenre?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
    }
}

