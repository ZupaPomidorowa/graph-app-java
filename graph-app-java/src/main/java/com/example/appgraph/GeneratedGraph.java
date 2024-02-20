package com.example.appgraph;

import java.io.PrintWriter;

/**
 * Interface for a graph generated at runtime.
 */
public interface GeneratedGraph {
    /**
     * Gets the number of rows in the graph.
     * @return number of rows
     */
    int getRows();

    /**
     * Gets the number of columns in the graph.
     * @return number of columns
     */
    int getColumns();

    /**
     * Gets the lower end of the weight range.
     * @return lower end of weight range
     */
    double getWeightLower();

    /**
     * Gets the upper end of the weight range.
     * @return upper end of weight range
     */
    double getWeightUpper();

    /**
     * Calculates the number of vertices in the graph.
     * @return number of vertices
     */
    int getGraphSize();

    /**
     * Gets the vertex of a given index.
     * @param index index of the vertex
     * @return vertex object
     */
    Vertex getVertex(int index);

    /**
     * Generates a random connected graph of set dimension and weight range.
     */
    void generateGraph();

    /**
     * Splits a connected graph into disconnected segments.
     */
    void splitGraph();

    /**
     * Writes the graph into a file.
     * @param writer provided PrintWriter
     */
    void writeGraph(PrintWriter writer);

    /**
     * Prints the graph contents to standard output.
     */
    void printGraph();
}