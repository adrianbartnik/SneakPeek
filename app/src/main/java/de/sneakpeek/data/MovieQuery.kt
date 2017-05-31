package de.sneakpeek.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import de.sneakpeek.util.createParcel

class MovieQuery(@Expose @SerializedName("page") var page: Int = 0,
                 @Expose @SerializedName("results") var results: List<MovieQueryResult>? = null,
                 @Expose @SerializedName("total_results") var totalResults: Int = 0,
                 @Expose @SerializedName("total_pages") var totalPages: Int = 0) : Parcelable {

    private constructor(source: Parcel) : this(
            source.readInt(),
            source.createTypedArrayList(MovieQueryResult.CREATOR),
            source.readInt(),
            source.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(page)
        dest.writeList(results)
        dest.writeValue(totalResults)
        dest.writeValue(totalPages)
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField @Suppress("unused")
        var CREATOR = createParcel { MovieQuery(it) }
    }
}