package de.sneakpeek.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso
import de.sneakpeek.R

import de.sneakpeek.data.MovieInfo
import de.sneakpeek.util.Util

class DetailActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(if (Util.GetUseLightTheme(application)) R.style.SneakPeekLightTransparentStatusBar else R.style.SneakPeekDarkTransparentStatusBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar = findViewById(R.id.activity_movie_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (!intent.extras.containsKey(MOVIE_KEY)) {
            Log.e(TAG, "This Activity has to be started via StartMovieActivity")
            return
        }

        val movie = intent.extras.getParcelable<MovieInfo>(MOVIE_KEY)

        val poster = findViewById(R.id.activity_movie_poster) as ImageView
        Picasso.with(this@DetailActivity)
//                .load(movie?.poster)
//                .placeholder(R.id.movie_list_item_position)
//                .into(poster)

        val director = findViewById(R.id.activity_main_director) as TextView
        director.text = movie.director

        val actors = findViewById(R.id.activity_main_actors) as TextView
//        actors.text = movie.actors

        val country = findViewById(R.id.activity_main_country) as TextView
//        country.text = movie.

        val genre = findViewById(R.id.activity_main_genre) as TextView
        genre.text = movie.genres[0].name

        val imdb_rating = findViewById(R.id.activity_main_imdb_rating) as TextView
        imdb_rating.text = movie.imdbId

        val metascore = findViewById(R.id.activity_main_metascore) as TextView
//        metascore.text = movie.sc

        val plot = findViewById(R.id.activity_main_plot) as TextView
        plot.text = movie.overview

        val released = findViewById(R.id.activity_main_released) as TextView
        released.text = movie.releaseDate

        val runtime = findViewById(R.id.activity_main_runtime) as TextView
        runtime.text = movie.runtime.toString()

        val writer = findViewById(R.id.activity_main_writer) as TextView
//        writer.text = movie.

        val collapsingToolbar = findViewById(R.id.activity_movie_collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.title = movie.title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private val TAG = DetailActivity::class.java.simpleName

        private val MOVIE_KEY = "MOVIE_KEY"

        fun StartMovieActivity(context: Context, movie: MovieInfo): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_KEY, movie)
            return intent
        }
    }
}
