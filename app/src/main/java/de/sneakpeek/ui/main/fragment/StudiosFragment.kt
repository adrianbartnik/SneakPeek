package de.sneakpeek.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import de.sneakpeek.R
import de.sneakpeek.adapter.StudiosAdapter
import de.sneakpeek.service.MovieRepository
import de.sneakpeek.util.inflate
import de.sneakpeek.view.FastScroll
import de.sneakpeek.view.FastScrollItemDecorator
import io.reactivex.disposables.CompositeDisposable

class StudiosFragment : Fragment() {

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private val studiosAdapter: StudiosAdapter by lazy { StudiosAdapter(emptyList()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = container?.inflate(R.layout.fragment_studios) as FastScroll

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = studiosAdapter

        recyclerView.addItemDecoration(FastScrollItemDecorator(context))
        recyclerView.itemAnimator = DefaultItemAnimator()

        return recyclerView
    }

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeDisposable()
        loadStudios()
    }

    override fun onStop() {
        super.onStop()
        subscriptions.dispose()
    }

    fun loadStudios() {
        val subscription = MovieRepository.getInstance(context).getStudios()
                ?.subscribe({
                    studiosAdapter.addAll(it)
                }) { throwable ->
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to fetch movie moviePredictions", throwable)
                }

        subscriptions.add(subscription)
    }

    companion object {

        private val TAG = MoviePredictionsFragment::class.java.simpleName

        fun newInstance(): StudiosFragment {
            return StudiosFragment()
        }
    }
}