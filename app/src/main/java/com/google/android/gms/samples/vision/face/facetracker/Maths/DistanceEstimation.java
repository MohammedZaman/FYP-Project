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

}
