package de.sneakpeek.view;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class FastScroll extends RecyclerView {

    public static final int SECTION_TEXT_WIDTH = 25;
    public static final int SECTION_TEXT_HEIGHT = 18;

    public float scaledWidth;
    public float scaledHeight;
    public Character[] sections;
    public float sx;
    public float sy;
    public Character section;
    public boolean showLetter = false;
    private boolean setupComplete = false;

    public FastScroll(Context context) {
        super(context);
    }

    public FastScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas c) {
        if (!setupComplete)
            setupThings();
        super.onDraw(c);
    }

    public void forceReSetup() {
        setupComplete = false;
    }

    private void setupThings() {

        scaledWidth = SECTION_TEXT_WIDTH * getResources().getDisplayMetrics().density;
        scaledHeight = SECTION_TEXT_HEIGHT * getResources().getDisplayMetrics().density;

        //create section text data
        Set<Character> sectionSet = ((FastScrollRecyclerViewInterface) getAdapter()).getMapIndex().keySet();
        List<Character> listSection = new ArrayList<>(sectionSet);

        // Removes items, if number of sections would not fit on screen
        while (getHeight() <= listSection.size() * scaledHeight) {
            listSection.remove(new Random().nextInt(listSection.size()));
        }

        // Sort sections as well, so Ö appears together with O and not at the end
        final Collator collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY); // a == A, a < Ä

        Collections.sort(listSection, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return collator.compare(o1.toString(), o2.toString());
            }
        });

        sections = new Character[listSection.size()];

        for (int i = 0; i < listSection.size(); i++) {
            sections[i] = listSection.get(i);
        }

        sx = this.getWidth() - this.getPaddingRight() - (float) (1.2 * scaledWidth);
        sy = (float) ((this.getHeight() - (scaledHeight * sections.length)) / 2.0);
        setupComplete = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight * sections.length))
                    return super.onTouchEvent(event);
                else {
                    // We touched the index bar
                    float yy = y - this.getPaddingTop() - getPaddingBottom() - sy;
                    int currentPosition = (int) Math.floor(yy / scaledHeight);
                    if (currentPosition < 0) currentPosition = 0;
                    if (currentPosition >= sections.length) currentPosition = sections.length - 1;
                    section = sections[currentPosition];
                    showLetter = true;
                    int positionInData = 0;
                    if (((FastScrollRecyclerViewInterface) getAdapter()).getMapIndex().containsKey(section))
                        positionInData = ((FastScrollRecyclerViewInterface) getAdapter()).getMapIndex().get(section);
                    this.scrollToPosition(positionInData);
                    FastScroll.this.invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                if (!showLetter && (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight * sections.length)))
                    return super.onTouchEvent(event);
                else {
                    float yy = y - sy;
                    int currentPosition = (int) Math.floor(yy / scaledHeight);
                    if (currentPosition < 0) currentPosition = 0;
                    if (currentPosition >= sections.length) currentPosition = sections.length - 1;
                    section = sections[currentPosition];
                    showLetter = true;
                    int positionInData = 0;
                    if (((FastScrollRecyclerViewInterface) getAdapter()).getMapIndex().containsKey(section))
                        positionInData = ((FastScrollRecyclerViewInterface) getAdapter()).getMapIndex().get(section);
                    this.scrollToPosition(positionInData);
                    FastScroll.this.invalidate();

                }
                break;

            }
            case MotionEvent.ACTION_UP: {
                Handler listHandler = new ListHandler();
                listHandler.sendEmptyMessageDelayed(0, 100);
                if (x < sx - scaledWidth || y < sy || y > (sy + scaledHeight * sections.length))
                    return super.onTouchEvent(event);
                else
                    return true;
            }
        }
        return true;
    }

    public interface FastScrollRecyclerViewInterface {
        HashMap<Character, Integer> getMapIndex();
    }

    private class ListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showLetter = false;
            FastScroll.this.invalidate();
        }
    }
}
