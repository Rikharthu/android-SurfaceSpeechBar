package com.example.rikharthu.alexaspeechbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlexaSpeechBarView view = findViewById(R.id.speechBarView);
        view.setState(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setState(0);
            }
        }, 0);
    }
}
