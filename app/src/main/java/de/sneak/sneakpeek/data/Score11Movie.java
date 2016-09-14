package de.sneak.sneakpeek.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Score11Movie {

    @NonNull public String title;
    @NonNull public List<String> studios;

    public Score11Movie(@NonNull String title) {
        this.title = title;
        studios = new ArrayList<>();
    }

    public Score11Movie(@NonNull String title, @NonNull List<String> studios) {
        this.title = title;
        this.studios = studios;
    }

    @Override
    public String toString() {
        return title + " - Studios: " + studios;
    }
}
