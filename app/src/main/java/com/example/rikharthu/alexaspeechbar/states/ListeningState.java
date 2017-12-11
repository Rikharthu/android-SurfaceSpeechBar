package com.example.rikharthu.alexaspeechbar.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ListeningState extends State {

    private final Paint mCyanPaint;

    public ListeningState() {
        mCyanPaint = new Paint();
        mCyanPaint.setAntiAlias(true);
        mCyanPaint.setColor(Color.CYAN);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        int left = (canvas.getWidth() - canvas.getWidth() / 3) / 2;
        canvas.drawRect(new Rect(left, 0, canvas.getWidth() - left, canvas.getHeight()),
                mCyanPaint);
    }

    @Override
    public void reset() {

    }
}
