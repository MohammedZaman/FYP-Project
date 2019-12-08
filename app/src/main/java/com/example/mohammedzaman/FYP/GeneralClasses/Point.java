/**
 * This class is used as object to store coordinates
 */
package com.example.mohammedzaman.FYP.GeneralClasses;

public class Point {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    private int x;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private  int y ;

    public int getIndex(){
        String total;
        if(x > 9){
              int b =  x * 10 - y;
              total = String.valueOf(b - 1);
        }
        if(y == 10){
           int result =   x * y;
            total = String.valueOf(result - 1);

        }
        else {
             total = String.valueOf(x) + String.valueOf(y);
             int output = Integer.parseInt(total );
            total = String.valueOf(output - 1);
        }
        return Integer.parseInt(total);
    }


}
