package com.google.android.gms.samples.vision.face.facetracker;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.samples.vision.face.facetracker.ShortestPathAlgorithm.Dijkstra;

import java.io.IOException;

public class Navigation extends AppCompatActivity {

    private TextView output;
    private EditText start;
    private EditText end;
    private Button findPath;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        start = (EditText)findViewById(R.id.textView4);
        end = (EditText)findViewById(R.id.textView3);
        findPath = (Button) findViewById(R.id.button2);
        output = (TextView) findViewById(R.id.textView3);



    }
}
