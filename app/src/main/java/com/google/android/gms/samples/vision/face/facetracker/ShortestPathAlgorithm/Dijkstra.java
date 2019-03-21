package com.google.android.gms.samples.vision.face.facetracker.ShortestPathAlgorithm;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Dijkstra {

    private static final String PURPLE = "\033[0;33m";  // PURPLE
    private static final String RESET = "\033[0m";

    private static int  graph [][]; // small matrix to start off with
    private static int infintiy =  Integer.MAX_VALUE; // initialise nodes with infinity
    private static int distance[]; //  to store the shortest distance from the source node
    private static int previous[];
    private  static int blocked = -1;
    private static int  outPutGrid[][];


    private static Hashtable<Integer, String> indexTable = new Hashtable<>(); // find node name with index
    private  static int Size;
    private static FileInputStream is;
    private static BufferedReader reader;
    private static final File file = new File("/Users/mohammedzaman/AndroidStudioProjects/FYP-Project/app/src/main/java/com/google/android/gms/samples/vision/face/facetracker/ShortestPathAlgorithm/matrix.txt");



    /*


0 3 0 3 0 6
3 0 0 1 3 0
0 3 0 0 2 3
3 1 0 0 1 2
0 3 2 1 0 5
6 0 3 2 5 0



     */

    /*


0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0
1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0
0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0
0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0 0
0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0 0
0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0 0 1 0
0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1
0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 1
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws IOException {

        Size = 30;



          graph = new int[Size][Size];
          distance = new int[Size];
          previous = new int[Size];
          outPutGrid = new int[10][3];


//          loadMatrix();
//          loadTable();

        for (int row=0; row < outPutGrid.length; row++) {


            for (int col = 0; col < outPutGrid[row].length; col++) {

                outPutGrid[row][col]  = -1;
            }
        }

          DynamicMatrix();
          DynamicLoadTable();


        outputMatrix();

//
//        blockNode(23);
//        blockNode(14);


        performDijksta(graph,0 , 29);



        performAStar(graph,0,29);
















    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void loadMatrix() throws IOException {
        List<String> lines = new ArrayList<>();
        if (file.exists()) {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while( (line = reader.readLine() ) != null) {
                lines.add(line);
            }
            for (int row=0; row < graph.length; row++) {

                String lineNum = lines.get(row);
                int[] result = toIntArray(lineNum.split(" "));
                for (int col = 0; col < graph[row].length; col++) {

                graph[row][col]  = result[col];
                }
            }
        }

    }

    private static void DynamicMatrix(){


            for (int row=0; row < graph.length; row++) {
                for (int col = 0; col < graph[row].length; col++) {
                    if (row == col - 1) {
                        graph[row][col] = 1;
                        if(col % 10 == 0){
                            graph[row][col] = 0;
                        }

                    }else if(row == col + 1){
                        graph[row][col] = 1;
                        if(row % 10 == 0){
                            graph[row][col] = 0;
                        }
                    }
                    else if(row == col + 10){
                    graph[row][col] = 1;

                    }
                    else if(row == col - 10){
                        graph[row][col] = 1;
                    }
                    else{
                        graph[row][col] = 0;
                    }

                   // x = rn.nextInt(10 - 1 + 1) + 1;

                }

            }
    }



    public static int getSize() throws IOException {
        List<String> lines = new ArrayList<>();
        if (file.exists()) {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.size();
    }

    public static int[] toIntArray(String line[]){
        Size = line.length;
        int[] num = new int[line.length];
        for(int i = 0; i < line.length; i ++){
            num[i] = Integer.parseInt(line[i]);

        }
        return num;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void outputMatrix(){
        System.out.print(System.lineSeparator());
        System.out.println("Adjacency Matrix ");
        for (int row=0; row < graph.length; row++)
        {
            System.out.print(System.lineSeparator());
            for (int col=0; col < graph[row].length; col++)
            {
                if(graph[row][col] == 1){
                    System.out.print(PURPLE + graph[row][col] +RESET + " ");
                }else {
                    System.out.print(RESET+graph[row][col] + " ");
                }

            }
        }
    }



    private static String findNode(int index){
        return indexTable.get(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void performDijksta(int graph[][], int src , int dest){
        int dist[] = new int[distance.length]; // The output array. dist[i] will hold
        // the shortest distance from src to i

//         isVisited[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean isVisited[] = new Boolean[distance.length];

        previous[src] = Integer.MAX_VALUE;

        // Initialize all distances as INFINITE and isVisited[] as false
        for (int i = 0; i < distance.length; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            isVisited[i] = false;
        }

        // Distance of source vertex
//         dist[src] = ManhattenD(src,dest);

         dist[src] = 0;


        // Find shortest path for all vertices
        for (int count = 0; count < distance.length-1; count++)
        {

            System.out.print(System.lineSeparator());
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minimumDistance(dist, isVisited);


            // Mark the picked vertex as processed
            isVisited[u] = true;
            if(u == dest){
                break;


            }



            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < distance.length; v++) {
              //  System.out.print(" "+dist[v]);
                if (!isVisited[v] && graph[u][v] != 0) { // if node not visited and there is a edge within the adjacency matrix

                    if (dist[u] != Integer.MAX_VALUE) { // the distance list  is not infinity

                        if (dist[u] + graph[u][v] < dist[v]) {
                            if(graph[u][v] != blocked) {// if the cost is smaller then the current value in dist
                                previous[v] = u;
//                               dist[v] =  graph[u][v] + ManhattenD(src,dest);
                             dist[v] = dist[u] + graph[u][v];


                            }


                        }
                    }
                }
            }
        }

        System.out.println("******************** Dijkstra's algorithm ********************");
        printUnvisited(isVisited);
        outputPath(src,dest);
        printSolution(dist,src);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void performAStar(int graph[][], int src , int dest){
        int dist[] = new int[distance.length]; // The output array. dist[i] will hold
        // the shortest distance from src to i

//         isVisited[i] will true if vertex i is included in shortest
        // path tree or shortest distance from src to i is finalized
        Boolean isVisited[] = new Boolean[distance.length];

        previous[src] = Integer.MAX_VALUE;

        // Initialize all distances as INFINITE and isVisited[] as false
        for (int i = 0; i < distance.length; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            isVisited[i] = false;
        }

        // Distance of source vertex
        dist[src] = ManhattenD(src,dest);

        // dist[src] = 0;


        // Find shortest path for all vertices
        for (int count = 0; count < distance.length-1; count++)
        {

            System.out.print(System.lineSeparator());
            // Pick the minimum distance vertex from the set of vertices
            // not yet processed. u is always equal to src in first
            // iteration.
            int u = minimumDistance(dist, isVisited);


            // Mark the picked vertex as processed
            isVisited[u] = true;
            if(u == dest){
                break;


            }



            // Update dist value of the adjacent vertices of the
            // picked vertex.
            for (int v = 0; v < distance.length; v++) {
                //  System.out.print(" "+dist[v]);
                if (!isVisited[v] && graph[u][v] != 0) { // if node not visited and there is a edge within the adjacency matrix

                    if (dist[u] != Integer.MAX_VALUE) { // the distance list  is not infinity

                        if (dist[u] + graph[u][v] < dist[v]) {
                            if(graph[u][v] != blocked) {// if the cost is smaller then the current value in dist
                                previous[v] = u;
                                dist[v] =  graph[u][v] + ManhattenD(src,dest);
                                // dist[v] = dist[u] + graph[u][v];


                            }


                        }
                    }
                }
            }
        }



        System.out.println("******************** A* Algorithm ********************");
        printUnvisited(isVisited);
        outputPath(src,dest);
        printSolution(dist,src);

    }

    public static void printUnvisited(Boolean isVisited[]){

        for(int i=0; i < isVisited.length; i++){
            if(isVisited[i] == false){
              //  System.out.println(" !!!!Visited "+findNode(i));

                Point xyNode = getCorrdinates(i);
                if(outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] != -2) {
                    outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = -1;
                }
               // System.out.print("////// X + y = " + xyNode.getX() +" " + xyNode.getY());

            }else if(isVisited[i] == true){
               // System.out.println(" Visited "+findNode(i));
                Point xyNode = getCorrdinates(i);
                if(outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] != -2) {
                    outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 1;
                }


              //  System.out.print("////// X + y = " + xyNode.getX() +" " + xyNode.getY());
            }
        }










    }

    private static void blockNode(int node){
        Point xyNode = getCorrdinates(node);
        outPutGrid[xyNode.getY()-1][xyNode.getX()-1] = -2;
        for (int row=0; row < graph.length; row++) {

            for (int col = 0; col < graph[row].length; col++) {

                if(row == node || col == node) {
                    if(graph[row][col] == 1) {
                        graph[row][col] = blocked;


                    }
                }
            }
        }



    }


    private static int [] loadArrayInfiity(int[] array){
        for (int i = 0; i < array.length; i++ ){
            array[i] = Integer.MAX_VALUE;
        }

        return array;
    }
    private static int[] FindPath(int src, int dest){
        int[] array = new int[Size];
        int [] path = loadArrayInfiity(array);
        int pred = dest;
        for(int x = 0 ; x < path.length; x++){
        for(int i = 0; i < path.length; i++) {

            if (i == pred) {
                path[i] = previous[i];
                pred = previous[i];

            }

        }


        }

        return path;
    }

    private static void outputPath(int src, int dest){
        int[] path  = FindPath(src,dest);
        Arrays.sort(path);
        System.out.print(System.lineSeparator());
        System.out.print("Path from " + findNode(src) + " to ");

       for (int i = 0; i < path.length; i++ ){

           if(path[i] != Integer.MAX_VALUE){
            //   System.out.print(findNode(path[i])+ " -> ");
               Point xyNode = getCorrdinates(path[i]);
               outPutGrid[xyNode.getY()-1][xyNode.getX()-1] = 2;

           }
       }
       if(graph[dest][dest] != -1) {
         System.out.print("" + findNode(dest));
         Point xyNode = getCorrdinates(dest);
         outPutGrid[xyNode.getY()-1][xyNode.getX()-1] = 2;
       }else{
           System.out.print(" No Path to " + findNode(dest));
       }

    }

    private static int minimumDistance(int distance[], Boolean isVisited[]) {

        infintiy = Integer.MAX_VALUE;
        int min_index=-1;


        for (int n = 0; n < distance.length; n++)
            if (isVisited[n] == false && distance[n] <= infintiy)
            {
                infintiy = distance[n];
                min_index = n;

            }


        return min_index;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void printSolution(int dist[],int src){

        OutputMaze();

        //System.out.println("Total Cost =  " + dist[0]);
    ///    Path(src);




//        System.out.print(System.lineSeparator());
//        System.out.println("Vertex   Distance from Source");
//        for (int i = 0; i < n; i++) {
//            System.out.println(findNode(i) + " tt " + dist[i]);
//
//        }
    }

    private static void printAllPaths(int src)
    {
        System.out.print(System.lineSeparator());
        System.out.print(System.lineSeparator());
        System.out.println("Paths ");
        for (int i = 0; i < distance.length; i++) {
         outputPath(src,i);

        }
    }

    public static int ManhattenD(int currentNode ,int destNode){
        Point cNode = getCorrdinates(currentNode);
        Point dNode = getCorrdinates(destNode);
        int x = Math.abs(cNode.getX() - dNode.getX());
        int  y = Math.abs(cNode.getY() - dNode.getY());
        int xy = x + y;
        return x + y;

    }

    public static int EuclideanD(int cNode ,int destNode){
        Point nodeA = getCorrdinates(cNode);
        Point nodeB = getCorrdinates(destNode);
        Point result = new Point();
        result.setY(Math.abs (nodeA.getY() - nodeB.getY()));
        result.setX(Math.abs (nodeA.getX()- nodeB.getX()));
        double distance = Math.sqrt((result.getY())*(result.getY()) +(result.getX())*(result.getX()));
        return (int) distance;
    }

    public static Point getCorrdinates(int node){
        for (int row=0; row < Size; row++) {
            if(row == node && node >= 0 && node  <= 9){
                  Point point = new Point();
                  point.setX(1);
                  point.setY(row+1);
               // System.out.println("X: " +point.getX() + "| Y: " + point.getY() );
                  return point;
            }else if(row == node && node >= 10 && node  <=  19){
                Point point = new Point();
                point.setX(2);
                point.setY(row - 10 + 1);
                //System.out.println(point.getX() + "|" + point.getY() );
                return point;
            }else if(row == node && node >= 20 && node <=  29){
                Point point = new Point();
                point.setX(3);
                point.setY(row - 20 + 1);
               // System.out.println(point.getX() + "|" + point.getY() );
                return point;
            }else if(row == node && node >= 30 && node <=  39){
                Point point = new Point();
                point.setX(4);
                point.setY(row - 30 + 1);
                // System.out.println(point.getX() + "|" + point.getY() );
                return point;
            }

        }
return null;
    }

    private static void DynamicLoadTable(){

        int x = 0;
        char[] Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String Letter = "A";
        int D = 1;



        for (int i = 0; i < Size; i++) {

            indexTable.put(i,String.valueOf(Letters[x]) + D );
            D++;

            int g = i+1 ;
            if(g % 10 == 0){
                x = x + 1;
                D = 1;

            }

        }


    }

    private static void loadTable(){
        indexTable.put(0,"A1");
        indexTable.put(1,"A2");
        indexTable.put(2,"A3");
        indexTable.put(3,"A4");
        indexTable.put(4,"A5");
        indexTable.put(5,"A6");
        indexTable.put(6,"A7");
        indexTable.put(7,"A8");
        indexTable.put(8,"A9");
        indexTable.put(9,"A10");

        indexTable.put(10,"B1");
        indexTable.put(11,"B2");
        indexTable.put(12,"B3");
        indexTable.put(13,"B4");
        indexTable.put(14,"B5");
        indexTable.put(15,"B6");
        indexTable.put(16,"B7");
        indexTable.put(17,"B8");
        indexTable.put(18,"B9");
        indexTable.put(19,"B10");

        indexTable.put(20,"C1");
        indexTable.put(21,"C2");
        indexTable.put(22,"C3");
        indexTable.put(23,"C4");
        indexTable.put(24,"C5");
        indexTable.put(25,"C6");
        indexTable.put(26,"C7");
        indexTable.put(27,"C8");
        indexTable.put(28,"C9");
        indexTable.put(29,"C10");

        indexTable.put(20,"C1");
        indexTable.put(21,"C2");
        indexTable.put(22,"C3");
        indexTable.put(23,"C4");
        indexTable.put(24,"C5");
        indexTable.put(25,"C6");
        indexTable.put(26,"C7");
        indexTable.put(27,"C8");
        indexTable.put(28,"C9");
        indexTable.put(29,"C10");

        indexTable.put(30,"D1");
        indexTable.put(31,"D2");
        indexTable.put(32,"D3");
        indexTable.put(33,"D4");
        indexTable.put(34,"D5");
        indexTable.put(35,"D6");
        indexTable.put(36,"D7");
        indexTable.put(37,"D8");
        indexTable.put(38,"D9");
        indexTable.put(39,"D10");

        indexTable.put(40,"E1");
        indexTable.put(41,"E2");
        indexTable.put(42,"E3");
        indexTable.put(43,"E4");
        indexTable.put(44,"E5");
        indexTable.put(45,"E6");
        indexTable.put(46,"E7");
        indexTable.put(47,"E8");
        indexTable.put(48,"E9");
        indexTable.put(49,"E10");

    }

    public static  void Path(int src){

        System.out.print(System.lineSeparator());
        System.out.print(System.lineSeparator());
        for (int row=0; row < outPutGrid.length; row++)
        {

            for (int col=0; col < outPutGrid[row].length; col++)
            {
                  if(outPutGrid[row][col] == 2){

                     if(col != 0){
                    String x =  Integer.toString(col) + Integer.toString(row);
                    System.out.print(findNode(Integer.parseInt(x)) + " -> ");

                    }

                    else if(src == 0){

                         System.out.print(findNode(col) + " -> ");

                     }
                     else if(col == 0 ){
                        System.out.print(findNode(col) + " -> ");
                     }




                  }




            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void OutputMaze(){
        System.out.print(System.lineSeparator());
        System.out.println("Maze");
        System.out.println("--------Key-------");
        System.out.println("X = Node not visited");
        System.out.println("V = Node Visited");
        System.out.println("# = Path from Source to Destination");
        System.out.println("@ = Obstacle ");

        for (int row=0; row < outPutGrid.length; row++)
         {
            System.out.print(System.lineSeparator());
                for (int col=0; col < outPutGrid[row].length; col++)
                     {
                       if(outPutGrid[row][col] == -1){

                           System.out.print("X" + " ");
                       }else if(outPutGrid[row][col] == -2){
                           System.out.print("@" + " ");

                       }else if(outPutGrid[row][col] == 1){
                           System.out.print("V" + " ");
                       }else if(outPutGrid[row][col] == 2){
                           System.out.print("#" + " ");

                       }




                     }
            }
     }




}
