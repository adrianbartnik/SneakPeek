package de.sneakpeek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.sneakpeek.R;
import de.sneakpeek.data.MoviePrediction;

public class StudiosAdapter extends RecyclerView.Adapter<StudiosAdapter.MovieViewHolder> {

    private List<StudioTitlesTuple> moviesByStudio;

    public StudiosAdapter() {
        moviesByStudio = new ArrayList<>();
    }

    public void addAll(List<MoviePrediction> score11MovieWithStudios) {

        HashMap<String, List<String>> studioHashmap = new HashMap<>(); // Studio to titles mapping

        for (MoviePrediction movie : score11MovieWithStudios) {

            for (String studio : movie.getStudios()) {
                if (studioHashmap.containsKey(studio)) {
                    studioHashmap.get(studio).add(movie.getTitle());
                } else {
                    List<String> titles = new ArrayList<>();
                    titles.add(movie.getTitle());
                    studioHashmap.put(studio, titles);
                }
            }
        }

        ArrayList<String> studios = new ArrayList<>(studioHashmap.keySet());

        Collections.sort(studios);

        this.moviesByStudio = new ArrayList<>();

        for (String studio : studios) {
            moviesByStudio.add(new StudioTitlesTuple(studio, studioHashmap.get(studio)));
        }

        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_studio_list_element, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        StudioTitlesTuple titleStudioTuple = moviesByStudio.get(position);
        holder.studios.setText(titleStudioTuple.studio);
        holder.title.setText( StringUtil.join(titleStudioTuple.titles, ", "));
    }

    @Override
    public int getItemCount() {
        return moviesByStudio.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView title, studios;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.studio_list_item_title);
            studios = (TextView) itemView.findViewById(R.id.studio_list_item_studio);
        }
    }

    private class StudioTitlesTuple {
        public String studio;
        public List<String> titles;

        public StudioTitlesTuple(String studio, List<String> titles) {
            this.studio = studio;
            this.titles = titles;
        }
    }
}
