package de.sneak.sneakpeek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.sneak.sneakpeek.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private final MovieViewHolder.ClickListener clickListener;
    private List<String> movieTitles;

    public MoviesAdapter(List<String> movieTitles, MovieViewHolder.ClickListener clickListener) {
        this.movieTitles = movieTitles;
        this.clickListener = clickListener;
    }

    public void addAll(List<String> movieTitles) {
        this.movieTitles.clear();
        this.movieTitles.addAll(movieTitles);
        notifyDataSetChanged();
    }

    public String getTitleStudioTuple(int position) {
        return movieTitles.get(position);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movie_list_element, parent, false);

        return new MovieViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.title.setText(movieTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return movieTitles.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;

        private ClickListener clickListener;

        public MovieViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.movie_list_item_title);

            this.clickListener = clickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClicked(getAdapterPosition());
            }
        }

        public interface ClickListener {
            void onItemClicked(int position);
        }
    }
}
