package com.example.appgraph;

/**
 * Class containing node constructor.
 */
public class NodeXY {
    /**
     * X coordinate.
     */
    int x;
    /**
     * Y coordinate.
     */
    int y;
    /**
     * Number of row.
     */
    int i;
    /**
     * Nuber of column.
     */
    int j;
    /**
     * Node number.
     */
    int nodeNumber;

    /**
     * Constructor for node
     * @param nodeNumber node number
     * @param x X coordinate
     * @param y Y coordinate
     * @param i number of row
     * @param j number of column
     */
    public NodeXY(int nodeNumber, int x, int y, int i, int j) {
        this.nodeNumber = nodeNumber;
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
    }
}
