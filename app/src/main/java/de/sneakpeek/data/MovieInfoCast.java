package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class MovieInfoCast implements Parcelable {

    public final static Parcelable.Creator<MovieInfoCast> CREATOR = new Creator<MovieInfoCast>() {

        public MovieInfoCast createFromParcel(Parcel in) {
            MovieInfoCast instance = new MovieInfoCast();
            instance.character = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MovieInfoCast[] newArray(int size) {
            return (new MovieInfoCast[size]);
        }

    };

    @Json(name = "character") public String character;
    @Json(name = "name") public String name;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(character);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }
}


