package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R
import de.sneakpeek.data.MovieInfoCast

class ActorsAdapter(private var cast: List<MovieInfoCast>) : RecyclerView.Adapter<ActorsAdapter.MovieViewHolder>() {

    fun addAll(cast: List<MovieInfoCast>) {
        this.cast = cast
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_detail_actor_list_element, parent, false)

        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val actor = cast[position]
        holder.name.text = actor.name
        holder.character.text = actor.character
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var character: TextView = itemView.findViewById(R.id.actor_list_item_character) as TextView
        var name: TextView = itemView.findViewById(R.id.actor_list_item_name) as TextView
    }
}
