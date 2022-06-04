package com.example.appgraph;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ReadGraph {
    int getGraphSize();

    Vertex getVertex(int index);

    void readGraph(FileReader file) throws IOException;

    void writeGraph(PrintWriter writer);

    void printGraph();

    int getRows();

    int getColumns();

    double getWeightLower();

    double getWeightUpper();

}
