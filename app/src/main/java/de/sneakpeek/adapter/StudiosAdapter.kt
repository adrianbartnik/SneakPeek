package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R
import de.sneakpeek.data.StudioPredictions

class StudiosAdapter(private var studios: List<StudioPredictions>) : RecyclerView.Adapter<StudiosAdapter.MovieViewHolder>() {

    fun addAll(studios: List<StudioPredictions>) {
        this.studios = studios.sortedBy { it.studioTitle }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_studio_list_element, parent, false)

        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val studioPrediction = studios[position]
        holder.studios.text = studioPrediction.studioTitle
        holder.title.text = TextUtils.join(", ", studioPrediction.movies)
    }

    override fun getItemCount(): Int {
        return studios.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.studio_list_item_title) as TextView
        var studios: TextView = itemView.findViewById(R.id.studio_list_item_studio) as TextView
    }
}
