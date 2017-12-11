package com.example.rikharthu.alexaspeechbar.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ThinkingState extends State {
    public static final int FINISH_FRAMES_COUNT = 16;

    private Paint mCyanPaint;
    private Paint mBgPaint;
    private int mBarWidth = 0;
    private int mStep = 32;
    private int mSkipFrames = 0;

    public ThinkingState() {
        mCyanPaint = new Paint();
        mCyanPaint.setAntiAlias(true);
        mCyanPaint.setColor(Color.CYAN);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.BLUE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);

        if (mSkipFrames > 0) {
            mSkipFrames--;
            canvas.drawColor(Color.CYAN);
        } else {
            int left = (canvas.getWidth() - mBarWidth) / 2;
            canvas.drawRect(new Rect(left, 0, canvas.getWidth() - left, canvas.getHeight()),
                    mCyanPaint);
            mBarWidth += mStep;
            if (mBarWidth >= canvas.getWidth()) {
                mBarWidth = canvas.getWidth() / 3;
                mSkipFrames = FINISH_FRAMES_COUNT;
            }
        }
    }

    @Override
    public void reset() {
        // TODO
    }
}
