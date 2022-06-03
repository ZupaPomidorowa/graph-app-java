package com.example.appgraph;

import java.util.Arrays;

public class Vertex {
    // maybe get rid of the index here
    private final int index;
    // 0 -> upper (i-column) neighbour
    // 1 -> left (i-1) neighbour
    // 2 -> right (i+1) neighbour
    // 3 -> lower (i+column) neighbour
    private final int [] neighbour = new int [4];
    private final double [] weight = new double [4];
    public final static int UPPER = 0;
    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    public final static int LOWER = 3;

    public Vertex(int index) {
        this.index = index;
        Arrays.fill(neighbour, -1);
        Arrays.fill(weight, -1);
    }

    public void setNeighbour(int position, int nIndex, double w) {
        neighbour[position] = nIndex;
        weight[position] = w;
    }

    public void removeNeighbour(int position) {
        neighbour[position] = -1;
        weight[position] = -1;
    }

    public int getNeighbour(int position) {
        return neighbour[position];
    }

    public boolean hasNeighbour(int position) {
        return (neighbour[position] != -1);
    }

    public int countNeighbours() {
        int count = 0;
        for(int i = 0; i < 4; i++) {
            if(neighbour[i] != -1) {
                count++;
            }
        }
        return count;
    }
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
