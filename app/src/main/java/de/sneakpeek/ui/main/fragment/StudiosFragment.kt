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
import rx.subscriptions.CompositeSubscription

class StudiosFragment : Fragment() {

    private var subscriptions: CompositeSubscription? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var studiosAdapter: StudiosAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        swipeRefreshLayout = inflater!!.inflate(R.layout.fragment_studios, container, false) as SwipeRefreshLayout

        swipeRefreshLayout?.setOnRefreshListener { setMovies(true) }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_studios_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        studiosAdapter = StudiosAdapter()
        recyclerView.adapter = studiosAdapter

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        subscriptions = CompositeSubscription()

        return swipeRefreshLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMovies(false)
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.unsubscribe()
    }

    fun setMovies(force: Boolean) {
        val subscription = MovieRepository.getInstance().fetchStudios(force)
                .subscribe({ movieRepository ->
                    studiosAdapter!!.addAll(movieRepository)
                    swipeRefreshLayout!!.isRefreshing = false
                }) { throwable ->
                    swipeRefreshLayout!!.isRefreshing = false
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to fetch movie predictions", throwable)
                }

        subscriptions!!.add(subscription)
    }

    companion object {

        private val TAG = MovieFragment::class.java.simpleName

        fun newInstance(): StudiosFragment {
            return StudiosFragment()
        }
    }
}