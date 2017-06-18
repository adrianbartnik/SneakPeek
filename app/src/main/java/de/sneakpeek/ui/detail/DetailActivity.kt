package de.sneakpeek.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import com.squareup.picasso.Picasso
import de.sneakpeek.R
import de.sneakpeek.adapter.ActorsAdapter
import de.sneakpeek.data.MovieInfo
import de.sneakpeek.service.TheMovieDBService
import de.sneakpeek.util.Util
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_movie_information.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(if (Util.GetUseLightTheme(application)) R.style.SneakPeekLightTransparentStatusBar else R.style.SneakPeekDarkTransparentStatusBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        activity_detail_toolbar.title = ""
        setSupportActionBar(activity_detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (!intent.extras.containsKey(MOVIE_KEY)) {
            Log.e(TAG, "This Activity has to be started via StartMovieActivity")
            return
        }

        val movie = intent.extras.getParcelable<MovieInfo>(MOVIE_KEY)

        Picasso.with(this@DetailActivity)
                .load("${TheMovieDBService.IMAGE_URL}${movie?.poster_path}")
                .placeholder(R.drawable.movie_poster_placeholder)
                .into(activity_detail_poster)

        val vote_average_fab = if (movie.vote_average.compareTo(0) == 0) {
            getString(R.string.activity_detail_vote_average_na)
        } else {
            "${movie.vote_average}"
        }
        activity_movie_fab.setImageBitmap(textAsBitmap(vote_average_fab, 40f, Color.WHITE))

        activity_detail_original_title.text = if (TextUtils.isEmpty(movie.original_title)) {
            ""
        } else {
            " - ${movie.original_title}"
        }

        activity_detail_director.text = movie.director

        activity_detail_original_language.text = Locale(movie.original_language).displayLanguage

        activity_detail_genre.text = movie?.genres?.map { it.name ?: "" }?.reduce { acc, s -> "$acc | $s" }

        activity_detail_vote_average.text = "${movie.vote_average} / 10"

        val numberFormatter = NumberFormat.getCurrencyInstance()
        numberFormatter.minimumFractionDigits = 0
        activity_detail_budget.text = if (movie.budget == 0) {
            getText(R.string.activity_detail_budget_na)
        } else {
            numberFormatter.format(movie.budget)
        }

        activity_detail_plot.text = movie.overview

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY).parse(movie.release_date)
        activity_detail_released.text = SimpleDateFormat.getDateInstance().format(format)

        activity_detail_runtime.text = "${movie.runtime} min"

        activity_detail_writer.text = movie.writer

        activity_detail_recycler_view_actors.setHasFixedSize(true)
        activity_detail_recycler_view_actors.adapter = ActorsAdapter(movie.credits?.cast ?: emptyList())
        activity_detail_recycler_view_actors.itemAnimator = DefaultItemAnimator()
        activity_detail_recycler_view_actors.layoutManager = LinearLayoutManager(baseContext)
        activity_detail_recycler_view_actors.isNestedScrollingEnabled = false
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

    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap {
        val paint = Paint(ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val image = Bitmap.createBitmap(paint.measureText(text).toInt(),
                baseline.toInt() + paint.descent().toInt(),
                Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }
}
