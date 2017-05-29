package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class MovieInfoCrew implements Parcelable {

    public final static Parcelable.Creator<MovieInfoCrew> CREATOR = new Creator<MovieInfoCrew>() {

        public MovieInfoCrew createFromParcel(Parcel in) {
            MovieInfoCrew instance = new MovieInfoCrew();
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.job = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MovieInfoCrew[] newArray(int size) {
            return (new MovieInfoCrew[size]);
        }

    };

    @Json(name = "name") public String name;
    @Json(name = "job") public String job;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(job);
    }

    public int describeContents() {
        return 0;
    }
}


