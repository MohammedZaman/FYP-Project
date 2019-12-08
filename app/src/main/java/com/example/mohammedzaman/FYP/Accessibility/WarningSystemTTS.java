package com.example.mohammedzaman.FYP.Accessibility;


import android.content.Context;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import com.example.mohammedzaman.FYP.DistanceEstimation.DistanceEstimation;

import java.util.Locale;

public class WarningSystemTTS {

    private TextToSpeech mTTS;
    private DistanceEstimation mDistEst;
    private Vibrator vibrator;
    private int warning1 = 0;


    public WarningSystemTTS (Context contextA, final String speechInit){
        mDistEst = new DistanceEstimation();
        vibrator = (Vibrator) contextA.getSystemService(contextA.VIBRATOR_SERVICE);
        this.mTTS = new TextToSpeech(contextA , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTTS.setLanguage(Locale.UK);
                    mTTS.setPitch(1.3f);
                    mTTS.setSpeechRate(1f);
                    if(speechInit != "") {
                        mTTS.speak(speechInit, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

            }
        });



    }

    public synchronized void WarningDistance(float x){
        double distance = Double.parseDouble(mDistEst.calculateDistanceLogRegres(x).replaceAll("\\D+",""));
        if(distance >= 0 && distance  <=  50.0) {
        mTTS.setLanguage(Locale.UK);
        mTTS.setPitch(1.3f);
        mTTS.setSpeechRate(1f);
        if(warning1 != 1) {
            vibrator.vibrate(1000);
            warning1 ++;
        }
        }

    }

    public void Warning(float x){
            mTTS.setLanguage(Locale.UK);
            mTTS.setPitch(1.3f);
            mTTS.setSpeechRate(1f);
            mTTS.speak("Person Detected " + mDistEst.calculateDistanceLogRegres(x), TextToSpeech.QUEUE_FLUSH, null);

    }

    public void resetWarnings(){
        this.warning1 = 0;

    }

    public void speak(String speech){
        mTTS.setLanguage(Locale.UK);
        mTTS.setPitch(1.3f);
        mTTS.setSpeechRate(1f);
        mTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void stop(){
        mTTS.shutdown();
    }

    public void pause(){
       mTTS.stop();
    }

}
