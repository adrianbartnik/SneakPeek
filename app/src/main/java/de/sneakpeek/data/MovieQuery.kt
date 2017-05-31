package de.sneakpeek.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieQuery(@Expose @SerializedName("page") var page: Int = 0,
                 @Expose @SerializedName("results") var results: List<MovieQueryResult>? = null,
                 @Expose @SerializedName("total_results") var totalResults: Int = 0,
                 @Expose @SerializedName("total_pages") var totalPages: Int = 0) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieQuery> = object : Parcelable.Creator<MovieQuery> {
            override fun createFromParcel(source: Parcel): MovieQuery = MovieQuery(source)
            override fun newArray(size: Int): Array<MovieQuery?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.createTypedArrayList(MovieQueryResult.CREATOR),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(page)
        dest.writeTypedList(results)
        dest.writeInt(totalResults)
        dest.writeInt(totalPages)
    }
}