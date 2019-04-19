package com.google.android.gms.samples.vision.face.facetracker.DistanceEstimation;

import java.text.DecimalFormat;

public class DistanceEstimation {
    private double[] Xaxis = new double[22];
    private double[] Yaxis = new double[22];
    private TrendLine logGraph;

    public DistanceEstimation(){
        logGraph = new LogTrendLine();

        // DataSet 3 Y
        Yaxis[0] = 430;
        Yaxis[1] = 316;
        Yaxis[2] = 247;
        Yaxis[3] = 201;
        Yaxis[4] = 161;
        Yaxis[5] = 142;
        Yaxis[6] = 122;
        Yaxis[7] = 108;
        Yaxis[8] = 103;
        Yaxis[9] = 92;
        Yaxis[10] = 85;
        Yaxis[11] = 72;
        Yaxis[12] = 71;
        Yaxis[13] = 64;
        Yaxis[14] = 62;
        Yaxis[15] = 59;
        Yaxis[16] = 57;
        Yaxis[17] = 54;
        Yaxis[18] = 51;
        Yaxis[19] = 50;
        Yaxis[20] = 51;
        Yaxis[21] = 27;


        // DataSet 1 X
        Xaxis[0] =  10;
        Xaxis[1] = 20;
        Xaxis[2] = 30;
        Xaxis[3] = 40;
        Xaxis[4] =  50;
        Xaxis[5] = 60;
        Xaxis[6] = 70;
        Xaxis[7] = 80;
        Xaxis[8] = 90;
        Xaxis[9] =  100;
        Xaxis[10] = 110;
        Xaxis[11] = 120;
        Xaxis[12] = 130;
        Xaxis[13] = 140;
        Xaxis[14] =  150;
        Xaxis[15] = 160;
        Xaxis[16] = 170;
        Xaxis[17] = 180;
        Xaxis[18] = 190;
        Xaxis[19] = 200;
        Xaxis[20] = 210;
        Xaxis[21] = 220;
        logGraph.setValues(Yaxis,Xaxis);


    }

    /**
     * This methods utilises Apache Commons library to perform a logarithmic regression.
     * this is done estimate the distance from the phone to a person.
     * The graph needs to filled with data this is done within the constructor.
     * @param faceWidthPx This parameter is the bounding Box width in pixels
     * @return an estimated distance is returned
     */
    public String calculateDistanceLogRegres(float faceWidthPx){
        int result =  (int)logGraph.predict(faceWidthPx);
        String resultString =  String.valueOf(result);
        resultString += " cm";
        if(result > 100 && result >= 0 ) {
            double resultToM = result / 100;
            String resultToMString = String.valueOf(resultToM);
            resultToMString += " m";
            return " at " + resultToMString;
        }else if(result >= 0) {
            return " at " + resultString;
        }else{return "outside of range";}

    }


    public String calculateDistanceLinear(float x){
        double result =  -1.385*x + 278.02;
        DecimalFormat df = new DecimalFormat("#.00");
        String resultString = df.format(result);
        resultString += " cm";
        if(result > 100) {
            double resultToM = result / 100;
            String resultToMString = df.format(resultToM);
            resultToMString += " m";
            return resultToMString;
        }

        return resultString;
    }


    public String calculateDistancePolynomial(float x){
//       y = 0.0119x2 - 3.8881x + 369.8

        double result ;
        double E  = 3.8881 * x;
        double d  =   0.0119 * x;
        double resD = Math.pow(d, 2);
        result = resD - E + 369.8;


        DecimalFormat df = new DecimalFormat("#.00");
        String resultString = df.format(result);
        resultString += " cm";
        if(result > 100) {
            double resultToM = result / 100;
            String resultToMString = df.format(resultToM);
            resultToMString += " m";
            return resultToMString;
        }
        return resultString;
    }

    public String calculateDistancelogDataSet1(float x){

        double result;
        double E  = -110.4;
        double resD = Math.log(x);
        double f =  620.7;
        result = E * resD + f;



        DecimalFormat df = new DecimalFormat("#.00");
        String resultString = df.format(result);
        resultString += " cm";
        if(result > 100) {
            double resultToM = result / 100;
            String resultToMString = df.format(resultToM);
            resultToMString += " m";
            return resultToMString;
        }
        return resultString;
    }

    public String calculateDistancelogDataSet2(float x){


        double result;
        double E  = -130;
        double resD = Math.log(x);
        double f =  711.77;
        result = E * resD + f;



        DecimalFormat df = new DecimalFormat("#.00");
        String resultString = df.format(result);
        resultString += " cm";
        if(result > 100) {
            double resultToM = result / 100;
            String resultToMString = df.format(resultToM);
            resultToMString += " m";
            return resultToMString;
        }
        return resultString;
    }


    /**
     * @param faceWidthPx This parameter is the bounding Box width in px
     * @return The estimation from the phone to person is returned
     */
    public String calculateDistancelogDataSet3(float faceWidthPx){

        // calculation
        double result;
        double n  = -118.3;
        double resLog = Math.log(faceWidthPx);
        double f =  652.36;
        result = n * resLog + f;

        // output to 2 decimal point
        DecimalFormat df = new DecimalFormat("#.00");
        String resultString = df.format(result);
        resultString += " cm";

        // conversion to meters
        if(result > 100 && result >= 0 ) {
            double resultToM = result / 100;
            String resultToMString = df.format(resultToM);
            resultToMString += " m";
            return " at " + resultToMString;
        }
        else if(result >= 0) {
        return " at " + resultString;
        }else
            {return "outside of range";}

    }




}
