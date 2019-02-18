package com.google.android.gms.samples.vision.face.facetracker.Maths;

import java.util.Date;

public class SpeedOfObstacle {


    private Date createdDate = new java.util.Date();

    private DistanceEstimation mDistEst = new DistanceEstimation();


    public synchronized double getSpeed(float x){
       double distance = Double.parseDouble(mDistEst.calculateDistancelogDataSet3Raw(x));
        if(distance >= 100.0 && distance  <=  90.0) {
            createdDate.setSeconds(0);
        }
        if (distance >=  50.0){
            int s = getSeconds(createdDate);
            return CalculateSpeed(s);
        }

          return -1;
    }



    private int getSeconds(Date startTime) {
        java.util.Date now = new java.util.Date();
        return (int)((now.getTime() - startTime.getTime()) / 1000);
    }

    private double CalculateSpeed(int seconds){
        int distance = 50;
        int sec = seconds;
        if(sec != 0) {
            return distance / sec;
        }
        return -1.0;
    }




}
