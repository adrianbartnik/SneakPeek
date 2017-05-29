package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

public class MovieQueryResult implements Parcelable {

    public final static Parcelable.Creator<MovieQueryResult> CREATOR = new Creator<MovieQueryResult>() {

        public MovieQueryResult createFromParcel(Parcel in) {
            MovieQueryResult instance = new MovieQueryResult();
            instance.posterPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.overview = ((String) in.readValue((String.class.getClassLoader())));
            instance.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.genreIds, (java.lang.Integer.class.getClassLoader()));
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.popularity = ((double) in.readValue((double.class.getClassLoader())));
            instance.voteCount = ((int) in.readValue((int.class.getClassLoader())));
            instance.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
            return instance;
        }

        public MovieQueryResult[] newArray(int size) {
            return (new MovieQueryResult[size]);
        }

    };
    @Json(name = "poster_path") public String posterPath;
    @Json(name = "adult") public boolean adult;
    @Json(name = "overview") public String overview;
    @Json(name = "release_date") public String releaseDate;
    @Json(name = "genre_ids") public List<Integer> genreIds = null;
    @Json(name = "id") public int id;
    @Json(name = "original_title") public String originalTitle;
    @Json(name = "original_language") public String originalLanguage;
    @Json(name = "title") public String title;
    @Json(name = "backdrop_path") public String backdropPath;
    @Json(name = "popularity") public double popularity;
    @Json(name = "vote_count") public int voteCount;
    @Json(name = "video") public boolean video;
    @Json(name = "vote_average") public double voteAverage;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(posterPath);
        dest.writeValue(adult);
        dest.writeValue(overview);
        dest.writeValue(releaseDate);
        dest.writeList(genreIds);
        dest.writeValue(id);
        dest.writeValue(originalTitle);
        dest.writeValue(originalLanguage);
        dest.writeValue(title);
        dest.writeValue(backdropPath);
        dest.writeValue(popularity);
        dest.writeValue(voteCount);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
    }

    public int describeContents() {
        return 0;
    }
}