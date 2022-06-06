package com.example.appgraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for checking the connectivity of a graph using the BFS algorithm.
 */
public class Connectivity {
    /**
     * Array containing the colour (state) of each vertex (white, gray or black).
     */
    private final int [] colour;

    /**
     * Colour corresponding to the "undiscovered" state of a vertex.
     */
    private final static int WHITE = 0;

    /**
     * Colour corresponding to the "discovered" state of a vertex.
     */
    private final static int GRAY = 1;

    /**
     * Colour corresponding to the "explored" state of a vertex.
     */
    private final static int BLACK = 2;

    /**
     * Initially marks all the vertices as undiscovered.
     * @param g graph the connectivity of which we want to check
     */
    public Connectivity(Graph g) {
        colour = new int [g.getGraphSize()];
        Arrays.fill(colour, WHITE);
    }

    /**
     * Performs the BFS algorithm on a given graph, saves the state of each vertex in the colour array.
     * @param g graph
     * @param startVertex vertex from which the BFS algorithm starts visiting vertices
     */
    private void bfs(Graph g, int startVertex) {
        colour[startVertex] = GRAY;

        Queue<Integer> q = new LinkedList<>();
        q.add(startVertex);

        while(!q.isEmpty()) {
            int currentVertex = q.remove();
            for(int i = 0; i < 4; i++) {
                int neighbour = g.getVertex(currentVertex).getNeighbour(i);
                if((neighbour != -1) && (colour[neighbour] == WHITE)) {
                    colour[neighbour] = GRAY;
                    q.add(neighbour);
                }
            }
            colour[currentVertex] = BLACK;
        }
    }

    /**
     * Checks if the graph is connected.
     * @param g graph the connectivity of which we want to check
     * @return true or false
     */
    public boolean isConnected(Graph g) {
        bfs(g, 0);
        for(int current : colour) {
            if(current != BLACK) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if 2 chosen vertices are connected (to be used by splitGraph).
     * @param g graph containing the 2 vertices
     * @param startVertex vertex from which the BFS algorithm starts visiting vertices
     * @param finishVertex vertex to be checked if connected to the start vertex
     * @return true or false
     */
    public boolean isConnected(Graph g, int startVertex, int finishVertex) {
        // used to check if 2 chosen vertices are connected (by splitGraph)
        bfs(g, startVertex);
        return (colour[finishVertex] == BLACK);
    }
}
