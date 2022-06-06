package com.example.appgraph;

import java.util.Arrays;

/**
 * Class containing 1 vertex.
 */
public class Vertex {
    // maybe get rid of the index here
    private final int index;

    /**
     * Array containing indexes of all of vertex's neighbours.
     * Order: upper (v - columns), left (v - 1), right (v + 1), lower (v + columns).
     * (-1) if no neighbour present at the position.
     */
    private final int [] neighbour = new int [4];

    /**
     * Array containing weights of all of vertex's edges
     * Order: upper (v - columns), left (v - 1), right (v + 1), lower (v + columns).
     * (-1) if no edge present at the position.
     */
    private final double [] weight = new double [4];

    /**
     * 0 -> UPPER mapping for intuitive use of neighbour[] and weight[] arrays.
     */
    public final static int UPPER = 0;

    /**
     * 1 -> LEFT mapping for intuitive use of neighbour[] and weight[] arrays.
     */
    public final static int LEFT = 1;

    /**
     * 2 -> RIGHT mapping for intuitive use of neighbour[] and weight[] arrays.
     */
    public final static int RIGHT = 2;

    /**
     * 3 -> LOWER mapping for intuitive use of neighbour[] and weight[] arrays.
     */
    public final static int LOWER = 3;

    /**
     * Creates an empty vertex of a given index.
     * @param index index of the vertex
     */
    public Vertex(int index) {
        this.index = index;
        Arrays.fill(neighbour, -1);
        Arrays.fill(weight, -1);
    }

    /**
     * Adds a neighbour of a given index and edge weight to the vertex.
     * @param position position of the neighbour in relation to the vertex
     * @param nIndex index of the neighbour
     * @param w weight of the edge between the vertex and the neighbour
     */
    public void setNeighbour(int position, int nIndex, double w) {
        neighbour[position] = nIndex;
        weight[position] = w;
    }

    /**
     * Removes a neighbour of the vertex.
     * @param position position of the neighbour in relation to the vertex
     */
    public void removeNeighbour(int position) {
        neighbour[position] = -1;
        weight[position] = -1;
    }

    /**
     * Gets a neighbour of the vertex.
     * @param position position of the neighbour in relation to the vertex
     * @return index of the neighbour
     */
    public int getNeighbour(int position) {
        return neighbour[position];
    }

    /**
     * Checks if the vertex has a neighbour.
     * @param position position of the neighbour in relation to the vertex
     * @return true or false
     */
    public boolean hasNeighbour(int position) {
        return (neighbour[position] != -1);
    }

    /**
     * Counts the number of neighbours of the vertex.
     * @return number of neighbours (range: 0-4)
     */
    public int countNeighbours() {
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(neighbour[i] != -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the edge weight of an edge of the vertex.
     * @param position position of the edge in relation to the vertex
     * @return weight of the edge
     */
    public double getWeight(int position) {
        return weight[position];
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < 4; i++) {
            if(neighbour[i] != -1) {
                str = str + neighbour[i] + " :" + weight[i] + " ";
            }
        }
        return str;
    }
}
