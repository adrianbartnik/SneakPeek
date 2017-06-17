package de.sneakpeek.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.sneakpeek.R
import de.sneakpeek.data.StudioPredictions
import de.sneakpeek.view.FastScroll
import java.text.Collator
import java.util.*
import kotlin.collections.HashMap

class StudiosAdapter(private var studios: List<StudioPredictions>) : FastScroll.FastScrollRecyclerViewInterface,
        RecyclerView.Adapter<StudiosAdapter.MovieViewHolder>() {

    private var mMapIndex: HashMap<Char, Int> = HashMap()

    override fun getMapIndex(): HashMap<Char, Int> = mMapIndex

    private fun calculateIndexesForName(items: List<String>): HashMap<Char, Int> {

        val indexMap = LinkedHashMap<Char, Int>()

        items.map { it[0] }.mapIndexed { index, character ->
            if (!indexMap.containsKey(character)) {
                indexMap.put(character, index)
            } }

        return indexMap
    }

    fun addAll(studios: List<StudioPredictions>) {

        val collator = Collator.getInstance(Locale.GERMAN)
        collator.strength = Collator.SECONDARY // a == A, a < Ä

        this.studios = studios.sortedWith(kotlin.Comparator { studio1, studio2 -> collator.compare(studio1.studioTitle, studio2.studioTitle) })
        this.mMapIndex = calculateIndexesForName(this.studios.map { it.studioTitle.capitalize() })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_studio_list_element, parent, false)

        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val studioPrediction = studios[position]
        holder.studios.text = studioPrediction.studioTitle
        holder.title.text = TextUtils.join("\n", studioPrediction.movies)
    }

    override fun getItemCount(): Int {
        return studios.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.studio_list_item_title) as TextView
        var studios: TextView = itemView.findViewById(R.id.studio_list_item_studio) as TextView
    }
}
