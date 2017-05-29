package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class MovieInfoProductionCompany implements Parcelable {

    public final static Parcelable.Creator<MovieInfoProductionCompany> CREATOR = new Creator<MovieInfoProductionCompany>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieInfoProductionCompany createFromParcel(Parcel in) {
            MovieInfoProductionCompany instance = new MovieInfoProductionCompany();
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MovieInfoProductionCompany[] newArray(int size) {
            return (new MovieInfoProductionCompany[size]);
        }

    };
    @Json(name = "name") public String name;
    @Json(name = "id") public int id;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}