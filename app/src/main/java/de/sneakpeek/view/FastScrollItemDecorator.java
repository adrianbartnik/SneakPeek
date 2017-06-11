package de.sneakpeek.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;

import de.sneakpeek.R;

public class FastScrollItemDecorator extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Paint textPaint = new Paint();

    public FastScrollItemDecorator(Context context) {
        mContext = context;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);

        float scaledWidth = ((FastScroll) parent).scaledWidth;
        float sx = ((FastScroll) parent).sx;
        float scaledHeight = ((FastScroll) parent).scaledHeight;
        float sy = ((FastScroll) parent).sy;
        Character[] sections = ((FastScroll) parent).sections;
        Character section = ((FastScroll) parent).section;
        boolean showLetter = ((FastScroll) parent).showLetter;

        // We draw the letter in the middle
        if (showLetter & section != null) {
            //overlay everything when displaying selected index Letter in the middle
            Paint overlayDark = new Paint();
            overlayDark.setColor(Color.BLACK);
            overlayDark.setAlpha(100);
            canvas.drawRect(0, 0, parent.getWidth(), parent.getHeight(), overlayDark);
            float middleTextSize = mContext.getResources().getDimension(R.dimen.fast_scroll_overlay_text_size);
            Paint middleLetter = new Paint();
            middleLetter.setColor(Color.WHITE);
            middleLetter.setTextSize(middleTextSize);
            middleLetter.setAntiAlias(true);
            middleLetter.setFakeBoldText(true);
            middleLetter.setStyle(Paint.Style.FILL);
            int xPos = (canvas.getWidth() - (int) middleTextSize) / 2;
            int yPos = (int) ((canvas.getHeight() / 2) - ((middleLetter.descent() + middleLetter.ascent()) / 2));

            canvas.drawText(section.toString(), xPos, yPos, middleLetter);
        }

        // draw index A-Z

        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < sections.length; i++) {
            if (showLetter & section != null && sections[i].equals(section)) {
                textPaint.setColor(Color.WHITE);
                textPaint.setAlpha(255);
                textPaint.setFakeBoldText(true);
                textPaint.setTextSize(scaledWidth / 2);
                canvas.drawText(sections[i].toString(),
                        sx + textPaint.getTextSize() / 2,
                        sy + parent.getPaddingTop() + scaledHeight * (i + 1),
                        textPaint);
                textPaint.setTextSize(scaledWidth);
                canvas.drawText("•",
                        sx - textPaint.getTextSize() / 3,
                        sy + parent.getPaddingTop() + scaledHeight * (i + 1) + scaledHeight / 3,
                        textPaint);
            } else {
                textPaint.setColor(Color.LTGRAY);
                textPaint.setAlpha(200);
                textPaint.setFakeBoldText(false);
                textPaint.setTextSize(scaledWidth / 2);
                canvas.drawText(sections[i].toString(),
                        sx + textPaint.getTextSize() / 2,
                        sy + parent.getPaddingTop() + scaledHeight * (i + 1),
                        textPaint);
            }
        }
    }
}
