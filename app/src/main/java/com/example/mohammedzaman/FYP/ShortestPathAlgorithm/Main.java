package com.example.mohammedzaman.FYP.ShortestPathAlgorithm;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.mohammedzaman.FYP.GeneralClasses.Point;
import com.example.mohammedzaman.FYP.ObstacleAvoidance.ObstacleLocation;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Main {



    private static Hashtable<Integer, String> indexTable; // find node name with index
    private static String findNode(int index){
        return indexTable.get(index);
    }

    private static void DynamicLoadTable(){
        indexTable = new Hashtable<>();
        int x = 0;
        char[] Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String Letter = "A";
        int D = 1;



        for (int i = 0; i < 260; i++) {

            indexTable.put(i, String.valueOf(Letters[x]) + D);
            D++;

            int g = i + 1;
            if (g % 10 == 0) {
                x = x + 1;
                D = 1;

            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) {
//        AStar aStar =  new AStar();
//        System.out.print(System.lineSeparator());
//        System.out.print(System.lineSeparator());
//        System.out.print(System.lineSeparator());
//        System.out.print(System.lineSeparator());
//
//        System.out.print(aStar.performAStar(0,15));
//        System.out.print(")))))))))))))))))))))))))))))))))" + aStar.getPath());



        CustomLinkedList linkedList = new CustomLinkedList();

        linkedList.insertSorted(10);
        linkedList.insertSorted(4);
        linkedList.insertSorted(3);
        linkedList.insertSorted(5);
        linkedList.insertSorted(5);




        linkedList.displayAll();

//        Point x = new Point();
//        x.setX(5);
//        x.setY(3);
//
//
//        ObstacleLocation s = null;
//        Point loc = new Point();
//        loc.setX(1);
//        loc.setY(1);
//        Point res = s.getObstacleCoordinateTest(300,1);
//        if(res != null) {
//            System.out.print("current Node = "+findNode(x.getIndex()) +" predicted Node = " + findNode(res.getIndex()) + " coordinates: X " + res.getX() + " Y " + res.getY());
//        }else{
//            System.out.print("not working");
//        }

    }


}