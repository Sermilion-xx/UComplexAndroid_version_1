package org.ucomplex.ucomplex.Model.Calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Created by Sermilion on 02/02/16.
 */
public class DayDecoratorSpan implements LineBackgroundSpan {

    private int radius;
    private int color;
    private int offset;

    public DayDecoratorSpan(int radius, int color) {
        this.radius = radius;
        this.color = color;
        this.offset = offset;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        canvas.drawCircle((left + right) / 2, bottom + radius, radius, paint);
        paint.setColor(oldColor);
    }
}