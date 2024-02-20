package com.example.appgraph;

import java.io.FileReader;
import java.io.IOException;

/**
 * Interface for a graph read from a file.
 */
public interface ReadGraph {
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
     * Reads a graph from a file.
     * @param reader provided FileReader
     * @throws IOException in case of an incorrect file format
     */
    void readGraph(FileReader reader) throws IOException;

    /**
     * Prints the graph contents to standard output.
     */
    void printGraph();
}
