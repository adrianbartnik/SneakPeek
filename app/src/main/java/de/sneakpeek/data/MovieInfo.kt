package de.sneakpeek.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieInfo(@Expose @SerializedName("adult") var adult: Boolean = false,
                     @Expose @SerializedName("genres") var genres: List<MovieInfoGenre>?,
                     @Expose @SerializedName("id") var id: Int = 0,
                     @Expose @SerializedName("budget") var budget: Int = 0,
                     @Expose @SerializedName("imdb_id") var imdb_id: String?,
                     @Expose @SerializedName("original_language") var original_language: String?,
                     @Expose @SerializedName("original_title") var original_title: String?,
                     @Expose @SerializedName("overview") var overview: String?,
                     @Expose @SerializedName("popularity") var popularity: Double = 0.toDouble(),
                     @Expose @SerializedName("poster_path") var poster_path: String?,
                     @Expose @SerializedName("production_companies") var productionCompanies: List<MovieInfoProductionCompany>?,
                     @Expose @SerializedName("release_date") var release_date: String?,
                     @Expose @SerializedName("runtime") var runtime: Int = 0,
                     @Expose @SerializedName("tagline") var tagline: String?,
                     @Expose @SerializedName("title") var title: String?,
                     @Expose @SerializedName("vote_average") var vote_average: Double = 0.toDouble(),
                     @Expose @SerializedName("vote_count") var vote_count: Int = 0,
                     @Expose @SerializedName("credits") var credits: MovieInfoCredits?) : Parcelable {

    var director: String = ""
        get() {
            return credits?.crew
                    ?.firstOrNull { it.job.equals("director", ignoreCase = true) }
                    ?.name
                    ?: ""
        }

    var writer: String = ""
        get() {
            return credits?.crew
                    ?.firstOrNull { it.job.equals("writer", ignoreCase = true) }
                    ?.name
                    ?: ""
        }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MovieInfo> = object : Parcelable.Creator<MovieInfo> {
            override fun createFromParcel(source: Parcel): MovieInfo = MovieInfo(source)
            override fun newArray(size: Int): Array<MovieInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.createTypedArrayList(MovieInfoGenre.CREATOR),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readString(),
            source.createTypedArrayList(MovieInfoProductionCompany.CREATOR),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readInt(),
            source.readParcelable<MovieInfoCredits>(MovieInfoCredits::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt((if (adult) 1 else 0))
        dest.writeTypedList(genres)
        dest.writeInt(id)
        dest.writeInt(budget)
        dest.writeString(imdb_id)
        dest.writeString(original_language)
        dest.writeString(original_title)
        dest.writeString(overview)
        dest.writeDouble(popularity)
        dest.writeString(poster_path)
        dest.writeTypedList(productionCompanies)
        dest.writeString(release_date)
        dest.writeInt(runtime)
        dest.writeString(tagline)
        dest.writeString(title)
        dest.writeDouble(vote_average)
        dest.writeInt(vote_count)
        dest.writeParcelable(credits, 0)
    }
}