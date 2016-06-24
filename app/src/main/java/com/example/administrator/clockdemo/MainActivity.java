package com.example.administrator.clockdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewConfiguration;

public class MainActivity extends AppCompatActivity {

    ClockView clockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockView= (ClockView) findViewById(R.id.clock_view);
    }
}
