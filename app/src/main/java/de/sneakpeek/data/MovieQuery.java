package de.sneakpeek.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("page")
    @Expose
    public int page;
    @SerializedName("results")
    @Expose
    public List<MovieQueryResult> results = null;
    @SerializedName("total_results")
    @Expose
    public int totalResults;
    @SerializedName("total_pages")
    @Expose
    public int totalPages;

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