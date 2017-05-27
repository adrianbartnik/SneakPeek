package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieInfoSpokenLanguage implements Parcelable {

    public final static Parcelable.Creator<MovieInfoSpokenLanguage> CREATOR = new Creator<MovieInfoSpokenLanguage>() {

        public MovieInfoSpokenLanguage createFromParcel(Parcel in) {
            MovieInfoSpokenLanguage instance = new MovieInfoSpokenLanguage();
            instance.iso6391 = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public MovieInfoSpokenLanguage[] newArray(int size) {
            return (new MovieInfoSpokenLanguage[size]);
        }

    };

    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @SerializedName("name")
    @Expose
    public String name;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso6391);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}