package com.example.appgraph;

import java.io.*;
import java.util.ArrayList;

public class Tests {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        // random graph generation test
        GeneratedGraph gg = new Graph(3, 4, 0, 10);
        gg.generateGraph();
        gg.printGraph();
        Connectivity bfsBefore = new Connectivity((Graph) gg);
        if(bfsBefore.isConnected((Graph) gg)) {
            System.out.println("The graph is connected.");
        } else {
            System.out.println("The graph is disconnected.");
        }
        gg.splitGraph();
        gg.printGraph();
        Connectivity bfsAfter = new Connectivity((Graph) gg);
        if(bfsAfter.isConnected((Graph) gg)) {
            System.out.println("The graph is connected.");
        } else {
            System.out.println("The graph is disconnected.");
        }

        // graph file reading
        /*ReadGraph rg = new Graph();
        try {
            FileReader reader = new FileReader("graph.txt");
            rg.readGraph(reader);
        } catch (IOException e) {
            System.out.println("Incorrect file format." + e);
        }
        // graph file writing test
        PrintWriter writer = new PrintWriter("graph.output", "UTF-8");
        rg.writeGraph(writer);
        rg.printGraph();
        // bfs test
        Connectivity bfs = new Connectivity((Graph) rg);
        if(bfs.isConnected((Graph) rg)) {
            System.out.println("The graph is connected.");
        } else {
            System.out.println("The graph is disconnected.");
        }
        // dijkstry test
        Path dijkstry = new Path((Graph) rg);
        int startVertex = 0;
        int finishVertex = 6;
        ArrayList<Integer> path = dijkstry.findPath((Graph) rg, startVertex, finishVertex);
        if(path != null) {
            System.out.print("Path between " + startVertex + " and " + finishVertex + ": ");
            for(int i = (path.size() - 1); i > 0; i--) {
                System.out.print(path.get(i) + "-");
            }
            System.out.println(path.get(0) + ".");
        }*/
    }
}
