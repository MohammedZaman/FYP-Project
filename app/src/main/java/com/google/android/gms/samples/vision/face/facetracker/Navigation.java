package com.google.android.gms.samples.vision.face.facetracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import java.util.Locale;

public class Navigation extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private Button topLeftBtn;
    private Button topRightBtn;
    private Button bottomLeftBtn;
    private Button bottomRigthBtn;

    // text to speech
    private TextToSpeech tts;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        final String explain = "There are four Locations Available, click on the screen to get the location names. To start the navigation hold on the location";

        // text to speech
        this.tts = new TextToSpeech(this, new OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    tts.setPitch(1.3f);
                    tts.setSpeechRate(1f);
                    tts.speak(explain, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });


        // top row buttons

        topLeftBtn= (Button)findViewById(R.id.optTlBtn);
        topRightBtn = (Button)findViewById(R.id.optTrBtn);

        topLeftBtn.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);

        // bottom buttons

        bottomLeftBtn= (Button)findViewById(R.id.optBlBtn);
        bottomRigthBtn = (Button)findViewById(R.id.optBrBtn);

        // click event
        bottomLeftBtn.setOnClickListener(this);
        bottomRigthBtn.setOnClickListener(this);

        // long click event
        bottomRigthBtn.setOnLongClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.optTlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 1");
                break;
            case R.id.optTrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 2");
                break;
            case R.id.optBlBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 3");
                break;
            case R.id.optBrBtn:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                speak("location 4");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.optTlBtn:
                break;
            case R.id.optTrBtn:
                break;
            case R.id.optBlBtn:
                break;
            case R.id.optBrBtn:
                Intent intent = new Intent(this, FaceTrackerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

    private void speak(String speachText){
        tts.setLanguage(Locale.UK);
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        tts.speak(speachText, TextToSpeech.QUEUE_FLUSH, null);

    }
}
