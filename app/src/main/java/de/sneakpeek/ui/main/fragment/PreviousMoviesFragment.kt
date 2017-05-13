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
import de.sneakpeek.adapter.PreviousMoviesAdapter
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.ui.detail.DetailActivity
import de.sneakpeek.util.DividerItemDecoration
import rx.subscriptions.CompositeSubscription
import java.util.*

class PreviousMoviesFragment : Fragment() {

    private var subscriptions: CompositeSubscription? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var moviesAdapter: PreviousMoviesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        swipeRefreshLayout = inflater?.inflate(R.layout.fragment_previous_movies, container, false) as SwipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener { setPreviousMovies(true) }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_previous_movies_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        moviesAdapter = PreviousMoviesAdapter(ArrayList<String>(), object : PreviousMoviesAdapter.PreviousMovieViewHolder.ClickListener {
            override fun onItemClicked(position: Int) {
                val title = moviesAdapter?.getTitle(position)

                val subscription = MovieRepository.getInstance().fetchFullMovieInformation(title, false)
                        .subscribe({ movie ->
                            if (movie.title == null) {
                                Toast.makeText(context, "Failed to retrieve information for " + title, Toast.LENGTH_SHORT).show()
                            } else {
                                startActivity(DetailActivity.StartMovieActivity(context, movie))
                            }
                        }) { throwable ->
                            Toast.makeText(context, "Failed to fetch information for " + title, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Failed to fetch information for " + title, throwable)
                        }

                subscriptions?.add(subscription)
            }
        })

        recyclerView.adapter = moviesAdapter

        subscriptions = CompositeSubscription()

        return swipeRefreshLayout
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
        setPreviousMovies(false)
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.unsubscribe()
    }

    fun setPreviousMovies(force: Boolean) {

        val subscription = MovieRepository.getInstance().fetchPreviousMovies(force).subscribe({ movieRepository ->
            moviesAdapter?.addAll(movieRepository.reversed())
            swipeRefreshLayout?.isRefreshing = false
        }) { throwable ->
            swipeRefreshLayout?.isRefreshing = false
            Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Failed to fetch movie predictions", throwable)
        }

        subscriptions?.add(subscription)
    }

    companion object {

        private val TAG = MovieFragment::class.java.simpleName

        fun newInstance(): PreviousMoviesFragment {
            return PreviousMoviesFragment()
        }
    }
}