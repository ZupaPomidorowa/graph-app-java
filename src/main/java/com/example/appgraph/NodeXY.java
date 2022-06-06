package com.example.appgraph;

/**
 * Class containing a vertex's position.
 */
public class NodeXY {
    /**
     * X coordinate on the canvas.
     */
    int x;
    /**
     * Y coordinate on the canvas.
     */
    int y;
    /**
     * Vertex's row position.
     */
    int row;
    /**
     * Vertex's column position.
     */
    int column;
    /**
     * Vertex's index.
     */
    int index;

    /**
     * Constructs a vertex's position info.
     * @param index vertex's index
     * @param x x coordinate
     * @param y y coordinate
     * @param row row position
     * @param column column position
     */
    public NodeXY(int index, int x, int y, int row, int column) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
    }
}
