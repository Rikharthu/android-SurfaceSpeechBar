package com.example.rikharthu.alexaspeechbar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rikharthu.alexaspeechbar.states.ListeningState;
import com.example.rikharthu.alexaspeechbar.states.SpeakingState;
import com.example.rikharthu.alexaspeechbar.states.State;
import com.example.rikharthu.alexaspeechbar.states.ThinkingState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.rikharthu.alexaspeechbar.AlexaSpeechBarView.AlexaState.STATE_LISTENING;
import static com.example.rikharthu.alexaspeechbar.AlexaSpeechBarView.AlexaState.STATE_SPEAKING;
import static com.example.rikharthu.alexaspeechbar.AlexaSpeechBarView.AlexaState.STATE_THINKING;

public class AlexaSpeechBarView extends SurfaceView implements SurfaceHolder.Callback {

    private int mWidth;
    private int mHeight;
    private int mListeningBarWidth;
    private int mCurrentListeningBarWidth;
    @AlexaState
    private float mStep;
    private int mState = 0;
    private State mThinkingState;
    private State mSpeakingState;
    private State mListeningState;

    private AlexaSpeechBarView.DrawThread mDrawThread;
    private int mCountdown = 50;
    private boolean mDrawPlain;

    public AlexaSpeechBarView(Context context) {
        super(context);
        init();
    }

    public AlexaSpeechBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlexaSpeechBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AlexaSpeechBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        mThinkingState = new ThinkingState();
        mSpeakingState = new SpeakingState();
        mListeningState = new ListeningState();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mDrawThread = new AlexaSpeechBarView.DrawThread(getHolder());
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
            case STATE_LISTENING:
                onDrawListening(canvas);
                break;
            case STATE_THINKING:
                onDrawThinking(canvas);
                break;
            case STATE_SPEAKING:
                onDrawSpeaking(canvas);
                break;
        }
    }

    private void onDrawSpeaking(Canvas canvas) {
        mSpeakingState.onDraw(canvas);
    }

    private void onDrawThinking(Canvas canvas) {
        mThinkingState.onDraw(canvas);
    }

    private void onDrawListening(Canvas canvas) {
        mListeningState.onDraw(canvas);
    }

    public void setState(@AlexaState int state) {
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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_LISTENING, STATE_THINKING, STATE_SPEAKING})
    public @interface AlexaState {
        int STATE_LISTENING = 0;
        int STATE_THINKING = 1;
        int STATE_SPEAKING = 2;
    }
}
