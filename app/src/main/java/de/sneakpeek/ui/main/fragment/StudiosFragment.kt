package de.sneakpeek.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import de.sneakpeek.R
import de.sneakpeek.adapter.StudiosAdapter
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.util.DividerItemDecoration
import io.reactivex.disposables.CompositeDisposable

class StudiosFragment : Fragment() {

    private var subscriptions: CompositeDisposable? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var studiosAdapter: StudiosAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        swipeRefreshLayout = inflater!!.inflate(R.layout.fragment_studios, container, false) as SwipeRefreshLayout

        swipeRefreshLayout?.setOnRefreshListener { loadStudios() }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_studios_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        studiosAdapter = StudiosAdapter(emptyList())
        recyclerView.adapter = studiosAdapter

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        subscriptions = CompositeDisposable()

        return swipeRefreshLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadStudios()
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.dispose()
    }

    fun loadStudios() {
        val subscription = MovieRepository.getInstance(context).getStudios()
                ?.doOnEvent { _, _ -> swipeRefreshLayout?.isRefreshing = false }
                ?.subscribe({
                    studiosAdapter?.addAll(it)
                }) { throwable ->
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to fetch movie moviePredictions", throwable)
                }

        subscriptions?.add(subscription)
    }

    companion object {

        private val TAG = MoviePredictionsFragment::class.java.simpleName

        fun newInstance(): StudiosFragment {
            return StudiosFragment()
        }
    }
}