package com.google.android.gms.samples.vision.face.facetracker.ShortestPathAlgorithm;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class Main {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) {
        AStar aStar =  new AStar();
        aStar.blockNode(15);
        aStar.performAStar(10,19);
    }
}

