package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieInfoGenre implements Parcelable {

    public final static Parcelable.Creator<MovieInfoGenre> CREATOR = new Creator<MovieInfoGenre>() {

        public MovieInfoGenre createFromParcel(Parcel in) {
            MovieInfoGenre instance = new MovieInfoGenre();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MovieInfoGenre[] newArray(int size) {
            return (new MovieInfoGenre[size]);
        }

    };

    @SerializedName("id") @Expose public int id;
    @SerializedName("name") @Expose public String name;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }
}

