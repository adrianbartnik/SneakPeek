package de.sneakpeek.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.sneakpeek.R
import de.sneakpeek.adapter.ActorsAdapter
import de.sneakpeek.data.MovieInfo
import de.sneakpeek.service.TheMovieDBService
import de.sneakpeek.util.Util
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(if (Util.GetUseLightTheme(application)) R.style.SneakPeekLightTransparentStatusBar else R.style.SneakPeekDarkTransparentStatusBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById(R.id.activity_detail_toolbar) as Toolbar
        toolbar.title = ""
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (!intent.extras.containsKey(MOVIE_KEY)) {
            Log.e(TAG, "This Activity has to be started via StartMovieActivity")
            return
        }

        val movie = intent.extras.getParcelable<MovieInfo>(MOVIE_KEY)

        Log.d(TAG, "Received $movie")

        val poster = findViewById(R.id.activity_detail_poster) as ImageView
        Picasso.with(this@DetailActivity)
                .load("${TheMovieDBService.IMAGE_URL}${movie?.poster_path}")
                .placeholder(R.drawable.movie_poster_placeholder)
                .into(poster)

        val director = findViewById(R.id.activity_detail_director) as TextView
        director.text = movie.director

        val language = findViewById(R.id.activity_detail_original_language) as TextView
        language.text = Locale(movie.original_language).displayLanguage

        val genre = findViewById(R.id.activity_detail_genre) as TextView
        genre.text = movie?.genres?.map { it.name ?: "" }?.reduce { acc, s -> "$acc | $s" }

        val vote_average = findViewById(R.id.activity_detail_vote_average) as TextView
        vote_average.text = "${movie.vote_average} / 10"

        val budget = findViewById(R.id.activity_detail_budget) as TextView
        budget.text = movie.budget.toString()

        val plot = findViewById(R.id.activity_detail_plot) as TextView
        plot.text = movie.overview

        val released = findViewById(R.id.activity_detail_released) as TextView
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).parse(movie.release_date)
        released.text = SimpleDateFormat.getDateInstance().format(format)

        val runtime = findViewById(R.id.activity_detail_runtime) as TextView
        runtime.text = "${movie.runtime} min"

        val writer = findViewById(R.id.activity_detail_writer) as TextView
        writer.text = movie.writer

        val actorsRecyclerView = findViewById(R.id.activity_detail_recycler_view_actors) as RecyclerView
        actorsRecyclerView.setHasFixedSize(true)
        actorsRecyclerView.adapter = ActorsAdapter(movie.credits?.cast ?: emptyList())
        actorsRecyclerView.itemAnimator = DefaultItemAnimator()
        actorsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
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
