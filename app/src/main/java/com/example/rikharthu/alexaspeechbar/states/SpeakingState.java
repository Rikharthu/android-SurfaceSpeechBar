package com.example.rikharthu.alexaspeechbar.states;

import android.graphics.Canvas;
import android.graphics.Color;

public class SpeakingState extends State {


    private int mStartColor;
    private int mEndColor;
    private int mCurrentColor;
    private int mCurrentG;
    private int mStep;

    public SpeakingState() {
        mStartColor = Color.BLUE;
        int r1 = (mStartColor >> 16) & 0xFF;
        int g1 = (mStartColor >> 8) & 0xFF;
        int b1 = (mStartColor >> 0) & 0xFF;
        mEndColor = Color.CYAN;
        int r2 = (mEndColor >> 16) & 0xFF;
        int g2 = (mEndColor >> 8) & 0xFF;
        int b2 = (mEndColor >> 0) & 0xFF;
        mCurrentColor = mStartColor;
        mCurrentG = g1;
        mStep = 7;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.rgb(0, mCurrentG, 255));
        mCurrentG += mStep;
        if (mCurrentG >= 255) {
            mCurrentG = 255;
            mStep = -mStep;
        } else if (mCurrentG <= 0) {
            mStep = -mStep;
            mCurrentG = 0;
        }
    }

    @Override
    public void reset() {

    }
}
