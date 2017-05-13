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
import de.sneakpeek.adapter.MoviesAdapter
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.ui.detail.DetailActivity
import de.sneakpeek.util.DividerItemDecoration
import rx.subscriptions.CompositeSubscription
import java.util.*

class MovieFragment : Fragment() {

    private var subscriptions: CompositeSubscription? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var moviesAdapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        swipeRefreshLayout = inflater?.inflate(R.layout.fragment_movies, container, false) as SwipeRefreshLayout

        swipeRefreshLayout?.setOnRefreshListener { setMovies(true) }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_movies_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        moviesAdapter = MoviesAdapter(ArrayList<String>(), object : MoviesAdapter.MovieViewHolder.ClickListener {
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

        return swipeRefreshLayout
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
        setMovies(false)
    }

    override fun onStop() {
        super.onStop()
        subscriptions?.unsubscribe()
    }

    fun setMovies(force: Boolean) {

        val subscription = MovieRepository.getInstance().fetchMovies(force).subscribe({ movieRepository ->
            moviesAdapter?.addAll(movieRepository)
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

        fun newInstance(): MovieFragment {
            return MovieFragment()
        }
    }
}
