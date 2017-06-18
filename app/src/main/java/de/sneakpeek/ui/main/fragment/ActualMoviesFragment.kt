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
import de.sneakpeek.adapter.ActualMoviesAdapter
import de.sneakpeek.data.ActualMovie
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.ui.detail.DetailActivity
import de.sneakpeek.util.DividerItemDecoration
import de.sneakpeek.util.inflate
import io.reactivex.disposables.CompositeDisposable

class ActualMoviesFragment : Fragment() {

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var moviesAdapter: ActualMoviesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        swipeRefreshLayout = container?.inflate(R.layout.fragment_actual_movies) as SwipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener { setPreviousMovies() }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_previous_movies_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        moviesAdapter = ActualMoviesAdapter(ArrayList<ActualMovie>(), object : ActualMoviesAdapter.PreviousMovieViewHolder.ClickListener {
            override fun onItemClicked(position: Int) {
                val title = moviesAdapter?.getTitle(position)

                swipeRefreshLayout?.isRefreshing = true

                val subscription = MovieRepository(context).fetchFullMovieInformation(title!!.title)
                        .subscribe({ movie ->
                            swipeRefreshLayout?.isRefreshing = false
                            if (movie.title == null) {
                                Toast.makeText(context, "Failed to retrieve information for " + title, Toast.LENGTH_SHORT).show()
                            } else {
                                startActivity(DetailActivity.StartMovieActivity(context, movie))
                            }
                        }) { throwable ->
                            swipeRefreshLayout?.isRefreshing = false
                            Toast.makeText(context, "Failed to fetch information for " + title, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Failed to fetch information for $throwable", throwable)
                        }

                subscriptions.add(subscription)
            }
        })

        recyclerView.adapter = moviesAdapter

        subscriptions = CompositeDisposable()

        return swipeRefreshLayout
    }

    override fun onStart() {
        super.onStart()
        subscriptions = CompositeDisposable()
        setPreviousMovies()
    }

    override fun onStop() {
        super.onStop()
        subscriptions.dispose()
    }

    fun setPreviousMovies() {

        val subscription = MovieRepository(context).getActual()
                .doOnEvent { _, _ -> swipeRefreshLayout?.isRefreshing = false }
                .subscribe({ movieRepository ->
                    moviesAdapter?.addAll(movieRepository)
                }) { throwable ->
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to fetch movies", throwable)
                }

        subscriptions.add(subscription)
    }

    companion object {

        private val TAG = MoviePredictionsFragment::class.java.simpleName

        fun newInstance(): ActualMoviesFragment {
            return ActualMoviesFragment()
        }
    }
}