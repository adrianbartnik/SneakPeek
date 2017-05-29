package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class MovieInfoProductionCountry implements Parcelable {

    public final static Parcelable.Creator<MovieInfoProductionCountry> CREATOR = new Creator<MovieInfoProductionCountry>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieInfoProductionCountry createFromParcel(Parcel in) {
            MovieInfoProductionCountry instance = new MovieInfoProductionCountry();
            instance.iso31661 = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MovieInfoProductionCountry[] newArray(int size) {
            return (new MovieInfoProductionCountry[size]);
        }

    };
    @Json(name = "iso_3166_1") public String iso31661;
    @Json(name = "name") public String name;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso31661);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
