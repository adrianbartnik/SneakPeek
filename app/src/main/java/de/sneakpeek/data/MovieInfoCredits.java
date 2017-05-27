package de.sneakpeek.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieInfoCredits implements Parcelable {

    public final static Parcelable.Creator<MovieInfoCredits> CREATOR = new Creator<MovieInfoCredits>() {

        public MovieInfoCredits createFromParcel(Parcel in) {
            MovieInfoCredits instance = new MovieInfoCredits();
            in.readList(instance.cast, (MovieInfoCast.class.getClassLoader()));
            return instance;
        }

        public MovieInfoCredits[] newArray(int size) {
            return (new MovieInfoCredits[size]);
        }

    };

    @SerializedName("cast")
    @Expose
    public List<MovieInfoCast> cast;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cast);
    }

    public int describeContents() {
        return 0;
    }
}


