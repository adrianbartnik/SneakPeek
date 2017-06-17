package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R
import de.sneakpeek.data.ActualMovie
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ActualMoviesAdapter(
        private var movieTitles: List<ActualMovie>,
        private val clickListener: PreviousMovieViewHolder.ClickListener) :
        RecyclerView.Adapter<ActualMoviesAdapter.PreviousMovieViewHolder>() {

    val calendar: Calendar = Calendar.getInstance()
    val dateFormat: DateFormat = SimpleDateFormat.getDateInstance()

    fun addAll(movieTitles: List<ActualMovie>) {
        this.movieTitles = movieTitles.reversed()
        notifyDataSetChanged()
    }

    fun getTitle(position: Int): ActualMovie {
        return movieTitles[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousMovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_previous_movie_list_element, parent, false)

        return PreviousMovieViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: PreviousMovieViewHolder, position: Int) {
        val movie = movieTitles[position]
        val date = movie.formatWeek()

        calendar.set(Calendar.YEAR, date.year)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.WEEK_OF_YEAR, date.week)

        holder.date.text = dateFormat.format(calendar.time)
        holder.title.text = movie.title
    }

    override fun getItemCount(): Int {
        return movieTitles.size
    }

    class PreviousMovieViewHolder internal constructor(itemView: View, private val clickListener: PreviousMovieViewHolder.ClickListener?) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var title: TextView = itemView.findViewById(R.id.previous_movie_title) as TextView
        internal var date: TextView = itemView.findViewById(R.id.previous_movie_date) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onItemClicked(adapterPosition)
        }

        interface ClickListener {
            fun onItemClicked(position: Int)
        }
    }
}
