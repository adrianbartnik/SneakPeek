package de.sneak.sneakpeek.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.sneak.sneakpeek.R;
import de.sneak.sneakpeek.data.Movie;
import de.sneak.sneakpeek.util.Util;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private static final String MOVIE_KEY = "MOVIE_KEY";

    public static Intent StartMovieActivity(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(MOVIE_KEY, movie);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTheme(Util.GetUseLightTheme(getApplication()) ? R.style.SneakPeekLight : R.style.SneakPeekDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_movie_toolbar));

        if (!getIntent().getExtras().containsKey(MOVIE_KEY)) {
            Log.e(TAG, "This Activity has to be started via StartMovieActivity");
            return;
        }

        Movie movie = getIntent().getExtras().getParcelable(MOVIE_KEY);

        ImageView poster = (ImageView) findViewById(R.id.activity_movie_poster);
        Picasso.with(MovieActivity.this)
                .load(movie.poster)
                .into(poster); // TODO Add placeholder

        TextView director = (TextView) findViewById(R.id.activity_main_director);
        director.setText(movie.director);

        TextView actors = (TextView) findViewById(R.id.activity_main_actors);
        actors.setText(movie.actors);

        TextView country = (TextView) findViewById(R.id.activity_main_country);
        country.setText(movie.country);

        TextView genre = (TextView) findViewById(R.id.activity_main_genre);
        genre.setText(movie.genre);

        TextView imdb_rating = (TextView) findViewById(R.id.activity_main_imdb_rating);
        imdb_rating.setText(movie.imdbRating);

        TextView metascore = (TextView) findViewById(R.id.activity_main_metascore);
        metascore.setText(movie.metascore);

        TextView plot = (TextView) findViewById(R.id.activity_main_plot);
        plot.setText(movie.plot);

        TextView released = (TextView) findViewById(R.id.activity_main_released);
        released.setText(movie.released);

        TextView runtime = (TextView) findViewById(R.id.activity_main_runtime);
        runtime.setText(movie.runtime);

        TextView writer = (TextView) findViewById(R.id.activity_main_writer);
        writer.setText(movie.writer);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.activity_movie_collapsing_toolbar);
        collapsingToolbar.setTitle(movie.title);
    }
}
