package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R


class PreviousMoviesAdapter(private val movieTitles: MutableList<String>, private val clickListener: PreviousMovieViewHolder.ClickListener) :
        RecyclerView.Adapter<PreviousMoviesAdapter.PreviousMovieViewHolder>() {

    fun addAll(movieTitles: List<String>) {
        this.movieTitles.clear()
        this.movieTitles.addAll(movieTitles)
        notifyDataSetChanged()
    }

    fun getTitle(position: Int): String {
        return movieTitles.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousMovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_previous_movie_list_element, parent, false)

        return PreviousMovieViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: PreviousMovieViewHolder, position: Int) {
        holder.date.text = "" + (position + 1) + "."
        holder.title.text = movieTitles[position]
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
