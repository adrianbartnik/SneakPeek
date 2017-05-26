package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R
import de.sneakpeek.data.Prediction


class MoviesAdapter(private var predicion: Prediction, private val clickListener: MovieViewHolder.ClickListener) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    fun addAll(predicion: Prediction) {
        this.predicion = predicion
        notifyDataSetChanged()
    }

    fun getTitle(position: Int): String {
        return predicion.movies.get(position).title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_list_element, parent, false)

        return MovieViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.position.text = "${position + 1}."
        holder.title.text = predicion.movies[position].title
    }

    override fun getItemCount(): Int {
        return predicion.movies.size
    }

    class MovieViewHolder internal constructor(itemView: View, private val clickListener: MovieViewHolder.ClickListener?)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var title: TextView = itemView.findViewById(R.id.movie_list_item_title) as TextView
        internal var position: TextView = itemView.findViewById(R.id.movie_list_item_position) as TextView

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
