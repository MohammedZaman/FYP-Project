package com.google.android.gms.samples.vision.face.facetracker.Accessibility;


import android.content.Context;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import com.google.android.gms.samples.vision.face.facetracker.Maths.DistanceEstimation;

import java.util.Locale;

public class WarningSystemTTS {

    private TextToSpeech tts;
    private DistanceEstimation mDistEst;
    private Vibrator v;
    private int warning1 = 0;
    private int warning2 = 0;
    private int warning3 = 0;
    private int warning4 = 0;


    public WarningSystemTTS (Context contextA){
        mDistEst = new DistanceEstimation();
          v  = (Vibrator) contextA.getSystemService(contextA.VIBRATOR_SERVICE);
        this.tts = new TextToSpeech(contextA , new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    tts.setPitch(1.3f);
                    tts.setSpeechRate(1f);
                }

            }
        });



    }

    public synchronized void WarningDistance(float x){
        double distance = Double.parseDouble(mDistEst.calculateDistancelogDataSet3Raw(x));
        if(distance >= 0 && distance  <=  50.0) {
        tts.setLanguage(Locale.UK);
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        if(warning1 != 1) {
            v.vibrate(500);
            warning1 ++;
        }
        }else if(distance >= 50.0 && distance  <=  100.0) {
            tts.setLanguage(Locale.UK);
            tts.setPitch(1.3f);
            tts.setSpeechRate(1f);
            if(warning2 != 1) {
                v.vibrate(300);
                warning2++;
            }
        }else if(distance >= 100.0 && distance  <=  150.0) {
            tts.setLanguage(Locale.UK);
            tts.setPitch(1.3f);
            tts.setSpeechRate(1f);
            if(warning3 != 1) {
                v.vibrate(200);
                warning3++;
            }
        }else if(distance >= 150.0 && distance  <=  200.0) {
        tts.setLanguage(Locale.UK);
        tts.setPitch(1.3f);
        tts.setSpeechRate(1f);
        if(warning4 != 1) {
            v.vibrate(100);
            warning4++;
        }
    }


    }

    public void Warning(float x){
            tts.setLanguage(Locale.UK);
            tts.setPitch(1.3f);
            tts.setSpeechRate(1f);
            tts.speak("Person Detected at "+ mDistEst.calculateDistancelogDataSet3(x), TextToSpeech.QUEUE_FLUSH, null);

    }

    public void resetWarnings(){
        this.warning1 = 0;
        this.warning2 = 0;
        this.warning3 = 0;
        this.warning4 = 0;


    }

}
