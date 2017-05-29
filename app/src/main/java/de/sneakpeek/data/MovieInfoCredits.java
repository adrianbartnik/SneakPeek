package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.List;

public class MovieInfoCredits implements Parcelable {

    public final static Parcelable.Creator<MovieInfoCredits> CREATOR = new Creator<MovieInfoCredits>() {

        public MovieInfoCredits createFromParcel(Parcel in) {
            MovieInfoCredits instance = new MovieInfoCredits();
            in.readList(instance.cast, (MovieInfoCast.class.getClassLoader()));
            in.readList(instance.crew, (MovieInfoCrew.class.getClassLoader()));
            return instance;
        }

        public MovieInfoCredits[] newArray(int size) {
            return (new MovieInfoCredits[size]);
        }

    };

    @Json(name = "cast") public List<MovieInfoCast> cast;
    @Json(name = "crew") public List<MovieInfoCrew> crew;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cast);
        dest.writeValue(crew);
    }

    public int describeContents() {
        return 0;
    }
}


