package com.google.android.gms.samples.vision.face.facetracker.DistanceEstimation;

public class LogTrendLine extends OLSTrendLine {
    @Override
    protected double[] xVector(double x) {
        return new double[]{1,Math.log(x)};
    }

    @Override
    protected boolean logY() {return false;}

}
