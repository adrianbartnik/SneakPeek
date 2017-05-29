package de.sneakpeek.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

public class MovieQuery implements Parcelable {

    public final static Parcelable.Creator<MovieQuery> CREATOR = new Creator<MovieQuery>() {

        public MovieQuery createFromParcel(Parcel in) {
            MovieQuery instance = new MovieQuery();
            instance.page = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.results, (de.sneakpeek.data.MovieQueryResult.class.getClassLoader()));
            instance.totalResults = ((int) in.readValue((int.class.getClassLoader())));
            instance.totalPages = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MovieQuery[] newArray(int size) {
            return (new MovieQuery[size]);
        }

    };

    @Json(name = "page") public int page;
    @Json(name = "results") public List<MovieQueryResult> results = null;
    @Json(name = "total_results") public int totalResults;
    @Json(name = "total_pages") public int totalPages;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return 0;
    }
}