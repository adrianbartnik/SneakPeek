package de.sneak.sneakpeek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.sneak.sneakpeek.R;

public class PreviousMoviesAdapter extends RecyclerView.Adapter<PreviousMoviesAdapter.MovieViewHolder> {

    private List<String> movieTitles;

    public PreviousMoviesAdapter(List<String> movieTitles) {
        this.movieTitles = movieTitles;
    }

    public void addAll(List<String> movieTitles) {
        this.movieTitles.clear();
        for (String title : movieTitles) {
            this.movieTitles.add(title);
            notifyItemInserted(movieTitles.indexOf(title));
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movie_list_element, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.title.setText(movieTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return movieTitles.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.movie_list_item_title);
        }
    }
}
