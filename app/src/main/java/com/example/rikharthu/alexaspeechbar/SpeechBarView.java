package com.example.rikharthu.alexaspeechbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SpeechBarView extends SurfaceView implements SurfaceHolder.Callback {

    private int mWidth;
    private int mHeight;
    private int mListeningBarWidth;
    private int mCurrentListeningBarWidth;
    private float mStep;
    private int mState = 0;

    private DrawThread mDrawThread;
    private int mCountdown = 50;
    private boolean mDrawPlain;

    public SpeechBarView(Context context) {
        super(context);
        init();
    }

    public SpeechBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeechBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SpeechBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mDrawThread = new DrawThread(getHolder());
        mDrawThread.setRunning(true);
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;

        mListeningBarWidth = width / 3;
        mCurrentListeningBarWidth = mListeningBarWidth;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        mDrawThread.setRunning(false);
        while (retry) {
            try {
                mDrawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private void onDrawSpeechBar(Canvas canvas) {
        // Thinking
        switch (mState) {
            case 0:
                onDrawListening(canvas);
                break;
            case 1:
                onDrawThinking(canvas);
                break;
            case 2:
                onDrawSpeaking(canvas);
                break;
        }
    }

    private void onDrawSpeaking(Canvas canvas) {

    }

    private void onDrawThinking(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);

        mStep = 40;
        if (mDrawPlain) {
            if (--mCountdown <= 0) {
                mDrawPlain = false;
            }
            canvas.drawColor(Color.CYAN);
        } else {
            canvas.drawColor(Color.BLUE);
            mCurrentListeningBarWidth += mStep;
            if (mCurrentListeningBarWidth > mWidth) {
                mCurrentListeningBarWidth = mListeningBarWidth;
                mCountdown = 10;
                mDrawPlain = true;
                canvas.drawColor(Color.CYAN);
            } else {
                int left = (mWidth - mCurrentListeningBarWidth) / 2;
                canvas.drawRect(new Rect(left, 0, mWidth - left, mHeight), paint);
            }
        }
    }

    private void onDrawListening(Canvas canvas) {

    }

    public void setState(int state) {
        mState = state;
    }

    private class DrawThread extends Thread {

        private int mSpeechWidth;
        private boolean mIsRunning = false;
        private SurfaceHolder mSurfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            mIsRunning = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (mIsRunning) {
                canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas(null);
                    if (canvas == null) {
                        continue;
                    }
                    onDrawSpeechBar(canvas);
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
