package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import de.sneakpeek.util.createParcel

class MovieQueryResult(@Expose @SerializedName("poster_path") var posterPath: String,
                       @Expose @SerializedName("adult") var adult: Boolean = false,
                       @Expose @SerializedName("overview") var overview: String,
                       @Expose @SerializedName("release_date") var releaseDate: String,
                       @Expose @SerializedName("id") var id: Int = 0,
                       @Expose @SerializedName("original_title") var originalTitle: String,
                       @Expose @SerializedName("original_language") var originalLanguage: String,
                       @Expose @SerializedName("title") var title: String,
                       @Expose @SerializedName("backdrop_path") var backdropPath: String,
                       @Expose @SerializedName("popularity") var popularity: Double = 0.toDouble(),
                       @Expose @SerializedName("vote_count") var voteCount: Int = 0,
                       @Expose @SerializedName("video") var video: Boolean = false,
                       @Expose @SerializedName("vote_average") var voteAverage: Double = 0.toDouble()) : Parcelable {

    private constructor(source: Parcel) : this(
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readInt(),
            1 == source.readInt(),
            source.readDouble()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(posterPath)
        dest.writeValue(adult)
        dest.writeValue(overview)
        dest.writeValue(releaseDate)
        dest.writeValue(id)
        dest.writeValue(originalTitle)
        dest.writeValue(originalLanguage)
        dest.writeValue(title)
        dest.writeValue(backdropPath)
        dest.writeValue(popularity)
        dest.writeValue(voteCount)
        dest.writeValue(video)
        dest.writeValue(voteAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField @Suppress("unused")
        var CREATOR = createParcel { MovieQueryResult(it) }
    }
}