package de.sneakpeek.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

import java.util.List;

public class MovieInfo implements Parcelable {

    public final static Parcelable.Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {

        public MovieInfo createFromParcel(Parcel in) {
            MovieInfo instance = new MovieInfo();
            instance.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
            instance.belongsToCollection = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.budget = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.genres, (de.sneakpeek.data.MovieInfoGenre.class.getClassLoader()));
            instance.homepage = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.imdbId = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
            instance.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.overview = ((String) in.readValue((String.class.getClassLoader())));
            instance.popularity = ((double) in.readValue((double.class.getClassLoader())));
            instance.posterPath = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.productionCompanies, (de.sneakpeek.data.MovieInfoProductionCompany.class.getClassLoader()));
            in.readList(instance.productionCountries, (de.sneakpeek.data.MovieInfoProductionCountry.class.getClassLoader()));
            instance.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.revenue = ((int) in.readValue((int.class.getClassLoader())));
            instance.runtime = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.spokenLanguages, (de.sneakpeek.data.MovieInfoSpokenLanguage.class.getClassLoader()));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.tagline = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
            instance.voteCount = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MovieInfo[] newArray(int size) {
            return (new MovieInfo[size]);
        }

    };
    @Json(name = "adult") public boolean adult;
    @Json(name = "backdrop_path") public String backdropPath;
    @Json(name = "belongs_to_collection") public Object belongsToCollection;
    @Json(name = "budget") public int budget;
    @Json(name = "genres") public List<MovieInfoGenre> genres = null;
    @Json(name = "homepage") public String homepage;
    @Json(name = "id") public int id;
    @Json(name = "imdb_id") public String imdbId;
    @Json(name = "original_language") public String originalLanguage;
    @Json(name = "original_title") public String originalTitle;
    @Json(name = "overview") public String overview;
    @Json(name = "popularity") public double popularity;
    @Json(name = "poster_path") public String posterPath;
    @Json(name = "production_companies") public List<MovieInfoProductionCompany> productionCompanies = null;
    @Json(name = "production_countries") public List<MovieInfoProductionCountry> productionCountries = null;
    @Json(name = "release_date") public String releaseDate;
    @Json(name = "revenue") public int revenue;
    @Json(name = "runtime") public int runtime;
    @Json(name = "spoken_languages") public List<MovieInfoSpokenLanguage> spokenLanguages = null;
    @Json(name = "status") public String status;
    @Json(name = "tagline") public String tagline;
    @Json(name = "title") public String title;
    @Json(name = "video") public boolean video;
    @Json(name = "vote_average") public double voteAverage;
    @Json(name = "vote_count") public int voteCount;
    @Json(name = "credits") public MovieInfoCredits credits;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(backdropPath);
        dest.writeValue(belongsToCollection);
        dest.writeValue(budget);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeList(productionCompanies);
        dest.writeList(productionCountries);
        dest.writeValue(releaseDate);
        dest.writeValue(revenue);
        dest.writeValue(runtime);
        dest.writeList(spokenLanguages);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
    }

    public int describeContents() {
        return 0;
    }

    public
    @Nullable
    String getDirector() {
        if (credits != null && credits.crew != null) {
            for (MovieInfoCrew crew : credits.crew) {
                if (crew.job.equalsIgnoreCase("director")) {
                    return crew.name;
                }
            }
        }

        return null;
    }
}