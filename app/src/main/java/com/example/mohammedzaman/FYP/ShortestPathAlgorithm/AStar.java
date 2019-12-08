package com.example.mohammedzaman.FYP.ShortestPathAlgorithm;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.mohammedzaman.FYP.GeneralClasses.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AStar {

    private static final String PURPLE = "\033[0;33m";  // PURPLE
    private static final String RESET = "\033[0m";

    private static int  graph [][]; // small matrix to start off with
    private static int infintiy =  Integer.MAX_VALUE; // initialise nodes with infinity
    private static int distance[]; //  to store the shortest distance from the source node
    private static int previous[];
    private  static int blocked = -1;
    private static int  outPutGrid[][];



    private static Hashtable<Integer, String> indexTable = new Hashtable<>(); // find node name with index
    private static int Size;



    private  String Path = "";
    private int weight = 0;


    public int getWeight() {
        return weight;
    }

    public  String getPath() {
        return Path;
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AStar(){
          Size = 200;
          graph = new int[Size][Size];
          distance = new int[Size];
          previous = new int[Size];
          outPutGrid = new int[10][20];


        for (int row=0; row < outPutGrid.length; row++) {


            for (int col = 0; col < outPutGrid[row].length; col++) {

                outPutGrid[row][col]  = -1;
            }
        }

        DynamicMatrix();
        DynamicLoadTable();
        outputMatrix();
    }



    /**
     * This method used to create Adjacency Matrix, the Size variable to at an be used to
     * manipulate the size
     */
    private static void DynamicMatrix(){
            for (int row=0; row < graph.length; row++) {
                for (int col = 0; col < graph[row].length; col++) {

                    // Connection to nodes on the top
                    if (row == col - 1) {
                        graph[row][col] = 1;
                        if(col % 10 == 0){
                            graph[row][col] = 0;
                        }

                    }


                    // Connection to nodes on the bottom
                    else if(row == col + 1){
                        graph[row][col] = 1;
                        if(row % 10 == 0){
                            graph[row][col] = 0;
                        }
                    }


                    // Connection to nodes on the left
                    else if(row == col + 10){
                    graph[row][col] = 1;
                    }


                    // Connection to nodes on the right
                    else if(row == col - 10){
                        graph[row][col] = 1;
                    }


                    // the nodes are connected
                    else{
                        graph[row][col] = 0;
                    }

                }

            }
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
                    System.out.print(PURPLE + graph[row][col] + RESET + " ");
                }else {
                    System.out.print(RESET+graph[row][col] + " ");
                }

            }
        }
    }


    /**
     * this method provides the human readable reference of a node
     * @param index the index of the node
     * @return human readable node name
     */
    public String findNode(int index){
        return indexTable.get(index);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String performAStar(int src, int dest){
        Path = "";
        weight = 0;

        // The dist[] Array will hold the distance from source to destination
        int dist[] = new int[distance.length];

        // The nodes which are finalised will be set as true in the isVisited[] Array
        Boolean isVisited[] = new Boolean[distance.length];

        previous[src] = Integer.MAX_VALUE;

        LinkedHashSet<Integer> previousList = new LinkedHashSet<Integer>();

        // Initialize all distances to infinity and isVisited[] as false
        for (int i = 0; i < distance.length; i++)
        {
            dist[i] = Integer.MAX_VALUE;
            isVisited[i] = false;
        }

        // distance from src to src is always 0
        dist[src] = 0;



        // Find shortest path for all vertices
        for (int count = 0; count < distance.length-1; count++)
        {

            // Pick the minimum distance nodes from the set of nodes not yet processed.

            // u is the src in first iteration.
            int u = minimumDistance(dist, isVisited);


            // Mark the selected node as processed
            isVisited[u] = true;


            // break if the destination is reached
            if(u == dest){
                break;
            }

            // Update dist value of the adjacent nodes of the picked nodes.
            for (int v = 0; v < distance.length; v++) {
                // if node not visited and there is a edge within the adj
                if (!isVisited[v] && graph[u][v] != 0) {
                    // the distance value is not infinity
                    if (dist[u] != Integer.MAX_VALUE) {
                        // if the cost is smaller than the heuristic value
                        if (manhattanD(v,dest) + graph[u][v] < dist[v]) {
                            if(graph[u][v] != blocked) {
                                // tracking previous node for path
                                previousList.add(u);
                                previous[v] = u;
                                System.out.print("-> £ "+ findNode(u));

                                // update dist the graph value and Manhattan Distance
                                dist[v] =  graph[u][v] + manhattanD(v,dest);

                            }
                        }
                    }
                }
            }
        }
        System.out.println("******************** A* Algorithm ********************");
        printUnvisited(isVisited);
        printSolution(dist,src);
        FindPath(src,dest);
        outputPathNew(src,dest,previousList);
        return Path;

    }

    private void printUnvisited(Boolean isVisited[]){

        for(int i=0; i < isVisited.length; i++){
            if(isVisited[i] == false){
              //  System.out.println(" !!!!Visited "+findNode(i));

                Point xyNode = getCoordinates(i);
                if(outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] != -2) {
                    outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = -1;
                }
               // System.out.print("////// X + y = " + xyNode.getX() +" " + xyNode.getY());

            }else if(isVisited[i] == true){
               // System.out.println(" Visited "+findNode(i));
                Point xyNode = getCoordinates(i);
                if(outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] != -2) {
                    outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 1;
                }


              //  System.out.print("////// X + y = " + xyNode.getX() +" " + xyNode.getY());
            }
        }










    }

    public void blockNode(int node){
        Point xyNode = getCoordinates(node);
        outPutGrid[xyNode.getY()-1][xyNode.getX()-1] = -2;
        for (int row=0; row < graph.length; row++) {

            for (int col = 0; col < graph[row].length; col++) {

                if(row == node || col == node) {

                    graph[row][col] = blocked;


                }
            }
        }



    }


    private int [] loadArrayInfiity(int[] array){
        for (int i = 0; i < array.length; i++ ){
            array[i] = Integer.MAX_VALUE;
        }

        return array;
    }
    private int[] FindPath(int src, int dest){

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

    public void unblockAllNode(){
        for (int row=0; row < graph.length; row++) {

            for (int col = 0; col < graph[row].length; col++) {

                if(graph[row][col] == blocked) {

                    graph[row][col] = 1;


                }
            }
        }
    }

    private void outputPathNew(int src, int dest, LinkedHashSet<Integer> pre) {
        Path +="Path from " + findNode(src) + " to " + findNode(dest) +": " ;
        for (Integer temp : pre) {
            Path +=  findNode(temp) + " -> ";
            weight++;
        }
        Path +=  findNode(dest);
    }


    private String outputPath(int src, int dest) {
        String pathOutput = "";
        if (src > dest) {

            int[] path = FindPath(src, dest);
            //Arrays.sort(path);
            System.out.print(System.lineSeparator());
            System.out.print("Path from " + findNode(src) + " to " + findNode(dest) + ": ");
            pathOutput += "Path from " + findNode(src) + " to " + findNode(dest) + ": ";

            for(int i = 0; i < path.length / 2; i++)
            {
                int temp = path[i];
                path[i] = path[path.length - i - 1];
                path[path.length - i - 1] = temp;
            }
            for (int i = 0; i < path.length; i++) {

                if (path[i] != Integer.MAX_VALUE) {
                    System.out.print(findNode(path[i]) + " -> ");
                    pathOutput += findNode(path[i]) + " -> ";
                    Point xyNode = getCoordinates(path[i]);
                    outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 2;

                }
            }

            if (graph[dest][dest] != -1) {
                System.out.print("" + findNode(dest));
                pathOutput += findNode(dest);
                Point xyNode = getCoordinates(dest);
                outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 2;
                return pathOutput;
            } else {
                System.out.print(" No Path to " + findNode(dest));
                pathOutput += " No Path to " + findNode(dest);
                return pathOutput;
            }

        } else {
        int[] path = FindPath(src, dest);
        Arrays.sort(path);
        System.out.print(System.lineSeparator());
        System.out.print("Path from " + findNode(src) + " to " + findNode(dest) + ": ");
        pathOutput += "Path from " + findNode(src) + " to " + findNode(dest) + ": ";

        for (int i = 0; i < path.length; i++) {

            if (path[i] != Integer.MAX_VALUE) {
                System.out.print(findNode(path[i]) + " -> ");
                pathOutput += findNode(path[i]) + " -> ";
                Point xyNode = getCoordinates(path[i]);
                outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 2;

            }
        }

        if (graph[dest][dest] != -1) {
            System.out.print("" + findNode(dest));
            pathOutput += findNode(dest);
            Point xyNode = getCoordinates(dest);
            outPutGrid[xyNode.getY() - 1][xyNode.getX() - 1] = 2;
            return pathOutput;
        } else {
            System.out.print(" No Path to " + findNode(dest));
            pathOutput += " No Path to " + findNode(dest);
            return pathOutput;
        }
    }
       /*
             for(int i = 0; i < Size; i++)
            System.out.print("|" + distance[i]);

        System.out.println("|");

        int j;
        for (int i = 0; i < Size; i++)
        {
            if (i!=0)
            {

                System.out.print("Path = " + findNode(i));
                j = i;
                do
                {
                    if(previous[j] != infintiy) {
                        j = previous[j];
                        System.out.print(" <- " + findNode(j));
                    }
                }
                while(j != 0);
            }
            System.out.println();
        }
       **/
    }

    private int minimumDistance(int distance[], Boolean isVisited[]) {
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
    private void printSolution(int dist[],int src){

        OutputMaze();

    }


    private static int manhattanD(int currentNode , int destNode){
        Point cNode = getCoordinates(currentNode);
        Point dNode = getCoordinates(destNode);
        int x = Math.abs(cNode.getX() - dNode.getX());
        int  y = Math.abs(cNode.getY() - dNode.getY());
        int xy = x + y;
        return x + y;

    }


    private static Point getCoordinates(int node){
        for (int row=0;  row < Size; row++) {
            if(row == node && node >= 0 && node  <= 9){
                  Point point = new Point();
                  point.setX(1);
                  point.setY(row+1);
                  return point;
            }else if(row == node && node >= 10 && node  <=  19){
                Point point = new Point();
                point.setX(2);
                point.setY(row - 10 + 1);
                return point;
            }else if(row == node && node >= 20 && node <=  29){
                Point point = new Point();
                point.setX(3);
                point.setY(row - 20 + 1);
                return point;
            }else if(row == node && node >= 30 && node <=  39){
                Point point = new Point();
                point.setX(4);
                point.setY(row - 30 + 1);
                return point;
            }else if(row == node && node >= 40 && node <=  49){
                Point point = new Point();
                point.setX(5);
                point.setY(row - 40 + 1);
                // System.out.println(point.getX() + "|" + point.getY() );
                return point;
            }else if(row == node && node >= 50 && node <=  59){
                Point point = new Point();
                point.setX(6);
                point.setY(row - 50 + 1);
                return point;
            }else if(row == node && node >= 60 && node <=  69){
                Point point = new Point();
                point.setX(7);
                point.setY(row - 60 + 1);
                return point;
            }else if(row == node && node >= 70 && node <=  79){
                Point point = new Point();
                point.setX(8);
                point.setY(row - 70 + 1);
                return point;
            }else if(row == node && node >= 80 && node <=  89){
                Point point = new Point();
                point.setX(9);
                point.setY(row - 80 + 1);
                // System.out.println(point.getX() + "|" + point.getY() );
                return point;
            }else if(row == node && node >= 90 && node <=  99){
                Point point = new Point();
                point.setX(10);
                point.setY(row - 90 + 1);
                return point;
            }
            else if(row == node && node >= 100 && node <=  109){
                Point point = new Point();
                point.setX(11);
                point.setY(row - 100 + 1);
                return point;
            }
            else if(row == node && node >= 110 && node <=  119){
                Point point = new Point();
                point.setX(12);
                point.setY(row - 110 + 1);
                return point;
            }
            else if(row == node && node >= 120 && node <=  129){
                Point point = new Point();
                point.setX(13);
                point.setY(row - 120 + 1);
                return point;
            }
            else if(row == node && node >= 130 && node <=  139){
                Point point = new Point();
                point.setX(14);
                point.setY(row - 130 + 1);
                return point;
            }
            else if(row == node && node >= 140 && node <=  149){
                Point point = new Point();
                point.setX(15);
                point.setY(row - 140 + 1);
                return point;
            }
            else if(row == node && node >= 150 && node <=  159){
                Point point = new Point();
                point.setX(16);
                point.setY(row - 150 + 1);
                return point;
            }
            else if(row == node && node >= 160 && node <=  169){
                Point point = new Point();
                point.setX(17);
                point.setY(row - 160 + 1);
                return point;
            }
            else if(row == node && node >= 170 && node <=  179){
                Point point = new Point();
                point.setX(18);
                point.setY(row - 170 + 1);
                return point;
            }
            else if(row == node && node >= 180 && node <=  189){
                Point point = new Point();
                point.setX(19);
                point.setY(row - 180 + 1);
                return point;
            }
            else if(row == node && node >= 190 && node <=  199){
                Point point = new Point();
                point.setX(19);
                point.setY(row - 190 + 1);
                return point;
            }

        }
return null;
    }



    private static void DynamicLoadTable(){
        int x = 0;
        char[] Letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int a = 1;

        for (int i = 0; i < Size; i++) {
            indexTable.put(i,String.valueOf(Letters[x]) + a );
            a++;
            int b = i+1 ;
            if(b % 10 == 0){
                x = x + 1;
                a = 1;
            }
        }
    }



    private void Path(int src){
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
