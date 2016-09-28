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
import de.sneak.sneakpeek.adapter.MoviesAdapter;
import de.sneak.sneakpeek.data.Movie;
import de.sneak.sneakpeek.service.MovieRepository;
import de.sneak.sneakpeek.ui.MovieActivity;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MovieFragment extends Fragment {

    private static final String TAG = MovieFragment.class.getSimpleName();

    private CompositeSubscription subscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MoviesAdapter moviesAdapter;

    public static MovieFragment newInstance() {
        return new MovieFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_movies, container, false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setMovies(true);
            }
        });

        RecyclerView recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.fragment_movies_recycler_view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        moviesAdapter = new MoviesAdapter(new ArrayList<String>(), new MoviesAdapter.MovieViewHolder.ClickListener() {
            @Override
            public void onItemClicked(int position) {
                final String title = moviesAdapter.getTitle(position);

                Subscription subscription = MovieRepository.getInstance().fetchFullMovieInformation(title, false)
                        .subscribe(new Action1<Movie>() {
                            @Override
                            public void call(Movie movie) {
                                if (movie.title == null) {
                                    Toast.makeText(getContext(), "Failed to retrieve information for " + title, Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(MovieActivity.StartMovieActivity(getContext(), movie));
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getContext(), "Failed to fetch information for " + title, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Failed to fetch information for " + title, throwable);
                            }
                        });

                subscriptions.add(subscription);
            }
        });

        recyclerView.setAdapter(moviesAdapter);

        return swipeRefreshLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscriptions = new CompositeSubscription();
        setMovies(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        subscriptions.unsubscribe();
    }

    public void setMovies(boolean force) {

        Subscription subscription = MovieRepository.getInstance().fetchMovies(force).subscribe(new Action1<List<String>>() {
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
