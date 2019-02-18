package com.google.android.gms.samples.vision.face.facetracker.ShortestPathAlogithm;

public class Dijkstra {


    private static int adjacencyMatrix [][] = new int[5][5]; // small matrix to start off with
    private static int infintiy = 999; // initialise nodes with infinity


    public static void main(String[] args) {

        for (int row=0; row < adjacencyMatrix.length; row++)
        {
            for (int col=0; col < adjacencyMatrix[row].length; col++)
            {
                adjacencyMatrix[row][col] = 0;

            }
        }


        for (int row=0; row < adjacencyMatrix.length; row++)
        {
            System.out.print(System.lineSeparator());
            for (int col=0; col < adjacencyMatrix[row].length; col++)
            {
                System.out.print(adjacencyMatrix[row][col] + " ");

            }
        }


    }
}
