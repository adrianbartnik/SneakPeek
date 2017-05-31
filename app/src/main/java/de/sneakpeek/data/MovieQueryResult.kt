package de.sneakpeek.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieQueryResult> = object : Parcelable.Creator<MovieQueryResult> {
            override fun createFromParcel(source: Parcel): MovieQueryResult = MovieQueryResult(source)
            override fun newArray(size: Int): Array<MovieQueryResult?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
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

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(posterPath)
        dest.writeInt((if (adult) 1 else 0))
        dest.writeString(overview)
        dest.writeString(releaseDate)
        dest.writeInt(id)
        dest.writeString(originalTitle)
        dest.writeString(originalLanguage)
        dest.writeString(title)
        dest.writeString(backdropPath)
        dest.writeDouble(popularity)
        dest.writeInt(voteCount)
        dest.writeInt((if (video) 1 else 0))
        dest.writeDouble(voteAverage)
    }
}