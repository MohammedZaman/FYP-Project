package com.google.android.gms.samples.vision.face.facetracker.Maths;

import java.text.DecimalFormat;

public class DistanceEstimation {

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


    public String calculateDistancelogDataSet3(float x){


        double result;
        double E  = -118.3;
        double resD = Math.log(x);
        double f =  652.36;
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







    public String calculateDistancelogDataSet3TTs(float x){


        double result;
        double E  = -118.3;
        double resD = Math.log(x);
        double f =  652.36;
        result = E * resD + f;



        //DecimalFormat df = new DecimalFormat("#.00");
        String resultString = String.valueOf(Math.round(result));
        resultString += " cm";
        if(result > 100) {
            double resultToM = Math.round(result / 100);
            String resultToMString = String.valueOf(resultToM);
            resultToMString += " m";
            return resultToMString;
        }
        return resultString;
    }

    public String calculateDistancelogDataSet3Raw(float x){


        double result;
        double E  = -118.3;
        double resD = Math.log(x);
        double f =  652.36;
        result = E * resD + f;



        return  String.valueOf(result);
    }

    public String calculateDistanceLogRegres(float x){

        TrendLine t = new LogTrendLine();
      double[] Xaxis = new double[22];
      double[] Yaxis = new double[22];


        Yaxis[0] =  360;
        Yaxis[1] = 300;
        Yaxis[2] = 250;
        Yaxis[3] = 230;
        Yaxis[4] =  200;
        Yaxis[5] = 170;
        Yaxis[6] = 140;
        Yaxis[7] = 120;
        Yaxis[8] = 110;
        Yaxis[9] =  100;
        Yaxis[10] = 90;
        Yaxis[11] = 85;
        Yaxis[12] = 80;
        Yaxis[13] = 70;
        Yaxis[14] =  65;
        Yaxis[15] = 60;
        Yaxis[16] = 60;
        Yaxis[17] = 57;
        Yaxis[18] = 55;
        Yaxis[19] = 50;
        Yaxis[20] = 50;
        Yaxis[21] = 50;

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





        t.setValues(Yaxis,Xaxis);





        return  String.valueOf(t.predict(x));
    }

}
