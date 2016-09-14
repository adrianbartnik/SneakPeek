package de.sneak.sneakpeek.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.sneak.sneakpeek.R;
import de.sneak.sneakpeek.adapter.PreviousMoviesAdapter;
import de.sneak.sneakpeek.service.MovieRepository;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class PreviousMoviesFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();

    private CompositeSubscription subscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PreviousMoviesAdapter moviesAdapter;

    public static PreviousMoviesFragment newInstance() {
        return new PreviousMoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_previous_movies, container, false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setPreviousMovies();
            }
        });

        RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.fragment_previous_movies_recycler_view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        moviesAdapter = new PreviousMoviesAdapter(new ArrayList<String>());

        recyclerView.setAdapter(moviesAdapter);

        subscriptions = new CompositeSubscription();

        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPreviousMovies();
    }

    @Override
    public void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
    }

    public void setPreviousMovies() {

        Subscription subscription = MovieRepository.getInstance().fetchPreviousMovies().subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> movieRepository) {
                moviesAdapter.addAll(movieRepository);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Failed to fetch movies", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to fetch movie predictions", throwable);
            }
        });

        subscriptions.add(subscription);
    }

}

