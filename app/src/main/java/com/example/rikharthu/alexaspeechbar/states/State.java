package com.example.rikharthu.alexaspeechbar.states;

import android.graphics.Canvas;

public abstract class State {
    public abstract void onDraw(Canvas canvas);

    public abstract void reset();
}
