package de.sneak.sneakpeek.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

        @SerializedName("Title")
        @Expose
        public String title;
        @SerializedName("Year")
        @Expose
        public String year;
        @SerializedName("Rated")
        @Expose
        public String rated;
        @SerializedName("Released")
        @Expose
        public String released;
        @SerializedName("Runtime")
        @Expose
        public String runtime;
        @SerializedName("Genre")
        @Expose
        public String genre;
        @SerializedName("Director")
        @Expose
        public String director;
        @SerializedName("Writer")
        @Expose
        public String writer;
        @SerializedName("Actors")
        @Expose
        public String actors;
        @SerializedName("Plot")
        @Expose
        public String plot;
        @SerializedName("Language")
        @Expose
        public String language;
        @SerializedName("Country")
        @Expose
        public String country;
        @SerializedName("Awards")
        @Expose
        public String awards;
        @SerializedName("Poster")
        @Expose
        public String poster;
        @SerializedName("Metascore")
        @Expose
        public String metascore;
        @SerializedName("imdbRating")
        @Expose
        public String imdbRating;
        @SerializedName("imdbVotes")
        @Expose
        public String imdbVotes;
        @SerializedName("imdbID")
        @Expose
        public String imdbID;
        @SerializedName("Type")
        @Expose
        public String type;
        @SerializedName("Response")
        @Expose
        public String response;

        @Override
        public String toString() {
            return title + " - " + director;
        }

//    {"Title":"Memento",
//            "Year":"2000",
//            "Rated":"R",
//            "Released":"25 May 2001",
//            "Runtime":"113 min",
//            "Genre":"Mystery, Thriller",
//            "Director":"Christopher Nolan",
//            "Writer":"Christopher Nolan (screenplay), Jonathan Nolan (short story \"Memento Mori\")",
//            "Actors":"Guy Pearce, Carrie-Anne Moss, Joe Pantoliano, Mark Boone Junior",
//            "Plot":"A man juggles searching for his wife's murderer and keeping his short-term memory loss from being an obstacle.",
//            "Language":"English",
//            "Country":"USA",
//            "Awards":"Nominated for 2 Oscars. Another 54 wins & 55 nominations.",
//            "Poster":"http://ia.media-imdb.com/images/M/MV5BMTc4MjUxNDAwN15BMl5BanBnXkFtZTcwMDMwNDg3OA@@._V1_SX300.jpg",
//            "Metascore":"80",
//            "imdbRating":"8.5",
//            "imdbVotes":"849,921",
//            "imdbID":"tt0209144",
//            "Type":"movie",
//            "Response":"True"}
}
