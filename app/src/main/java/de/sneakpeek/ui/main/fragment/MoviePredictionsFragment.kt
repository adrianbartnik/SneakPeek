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
import de.sneakpeek.data.Prediction
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.ui.detail.DetailActivity
import de.sneakpeek.util.DividerItemDecoration
import de.sneakpeek.util.inflate
import io.reactivex.disposables.CompositeDisposable

class MoviePredictionsFragment : Fragment() {

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var moviesAdapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        swipeRefreshLayout = container?.inflate(R.layout.fragment_movies) as SwipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener { setMovies() }

        val recyclerView = swipeRefreshLayout?.findViewById(R.id.fragment_movies_recycler_view) as RecyclerView

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST)
        recyclerView.addItemDecoration(itemDecoration)

        moviesAdapter = MoviesAdapter(Prediction("", emptyList()), object : MoviesAdapter.MovieViewHolder.ClickListener {
            override fun onItemClicked(position: Int) {
                val title = moviesAdapter?.getTitle(position)

                Toast.makeText(context, title, Toast.LENGTH_LONG).show()

                val subscription = MovieRepository.getInstance(context).fetchFullMovieInformation(title!!)
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

                subscriptions.add(subscription)
            }
        })

        recyclerView.adapter = moviesAdapter

        return swipeRefreshLayout
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
        setMovies()
    }

    override fun onStop() {
        super.onStop()
        subscriptions.dispose()
    }

    fun setMovies() {

        val subscription = MovieRepository.getInstance(context).getMovies()
                .doOnEvent { _, _ -> swipeRefreshLayout?.isRefreshing = false }
                .subscribe({
                    moviesAdapter?.addAll(it)
                }) { throwable ->
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to fetch movie moviePredictions", throwable)
                }

        subscriptions.add(subscription)
    }

    companion object {

        private val TAG = MoviePredictionsFragment::class.java.simpleName

        fun newInstance(): MoviePredictionsFragment {
            return MoviePredictionsFragment()
        }
    }
}
