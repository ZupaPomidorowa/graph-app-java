package com.example.appgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class containing 1 graph.
 */
public class Graph implements GeneratedGraph, ReadGraph {
    /**
     * Number of rows in the graph.
     */
    private int rows;

    /**
     * Number of columns in the graph.
     */
    private int columns;

    /**
     * Array containing all vertices.
     */
    private Vertex [] v;

    /**
     * Lower end of the range of possible weight values.
     */
    private double weightLower;

    /**
     * Upper end of the range of possible weight values.
     */
    private double weightUpper;

    /**
     * Random seed for random weight and vertex generation.
     */
    private final Random r = new Random();

    /**
     * Creates an empty graph of given dimensions and weight range (to be generated randomly).
     * @param rows number of rows in the graph
     * @param columns number of columns in the graph
     * @param w1 lower end of the weight range
     * @param w2 upper end of the weight range
     */
    public Graph(int rows, int columns, double w1, double w2) {
        // generated randomly
        this.rows = rows;
        this.columns = columns;
        this.weightLower = w1;
        this.weightUpper = w2;
        v = new Vertex[rows * columns];
        for(int i = 0; i < getGraphSize(); i++)
            v[i] = new Vertex();
    }

    /**
     * Creates an empty graph of unknown dimensions (to be read from a file).
     */
    public Graph() {
        rows = columns = 0;
        weightLower = Double.POSITIVE_INFINITY;
        weightUpper = -1;
    }

    /**
     * Calculates the number of vertices in the graph.
     * @return number of vertices
     */
    @Override
    public int getGraphSize() {
        return rows * columns;
    }

    /**
     * Calculates the row in which a vertex is located.
     * @param index index of the vertex
     * @return number of row in which the vertex is located
     */
    private int getCurrentRow(int index) {
        return index / columns;
    }

    /**
     * Calculates the column in which a vertex is located.
     * @param index index of the vertex
     * @return number of column in which the vertex is located
     */
    private int getCurrentColumn(int index) {
        return index % columns;
    }

    /**
     * Generates a random weight in a set range.
     * @return weight value
     */
    private double getRandomWeight() {
        return weightLower + (weightUpper - weightLower) * r.nextDouble();
    }

    /**
     * Generates a random vertex index from the graph.
     * @return vertex index
     */
    private int getRandomVertex() {
        return r.nextInt(getGraphSize());
    }

    /**
     * Gets the number of rows in the graph.
     * @return number of rows
     */
    @Override
    public  int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the graph.
     * @return number of columns
     */
    @Override
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the lower end of the weight range.
     * @return lower end of weight range
     */
    @Override
    public double getWeightLower() {
        return weightLower;
    }

    /**
     * Gets the upper end of the weight range.
     * @return upper end of weight range
     */
    @Override
    public double getWeightUpper() {
        return weightUpper;
    }

    /**
     * Gets the vertex of a given index.
     * @param index index of the vertex
     * @return vertex object
     */
    @Override
    public Vertex getVertex(int index) {
        return v[index];
    }

    /**
     * Calculates and sets the weight range of a graph read from a file.
     */
    private void setWeightRange() {
        for(Vertex current : v) {
            for(int j = 0; j < 4; j++) {
                double w = current.getWeight(j);
                if(current.hasNeighbour(j) && (w < weightLower)) {
                    weightLower = current.getWeight(j);
                }
                if(w > weightUpper) {
                    weightUpper = current.getWeight(j);
                }
            }
        }
    }

    /**
     * Generates a random connected graph of set dimension and weight range.
     */
    @Override
    public void generateGraph() {
        double w;
        for(int i = 0; i < getGraphSize(); i++) {
            if((getCurrentRow(i) != 0) && !(v[i].hasNeighbour(Vertex.UPPER))) {
                w = getRandomWeight();
                v[i].setNeighbour(Vertex.UPPER, i - columns, w);
                v[i - columns].setNeighbour(Vertex.LOWER, i, w);
            }
            if((getCurrentColumn(i) != 0) && !(v[i].hasNeighbour(Vertex.LEFT))) {
                w = getRandomWeight();
                v[i].setNeighbour(Vertex.LEFT, i - 1, w);
                v[i - 1].setNeighbour(Vertex.RIGHT, i, w);
            }
            if((getCurrentColumn(i) != (columns - 1)) && !(v[i].hasNeighbour(Vertex.RIGHT))) {
                w = getRandomWeight();
                v[i].setNeighbour(Vertex.RIGHT, i + 1, w);
                v[i + 1].setNeighbour(Vertex.LEFT, i, w);
            }
            if((getCurrentRow(i) != (rows - 1)) && !(v[i].hasNeighbour(Vertex.LOWER))) {
                w = getRandomWeight();
                v[i].setNeighbour(Vertex.LOWER, i + columns, w);
                v[i + columns].setNeighbour(Vertex.UPPER, i, w);
            }
        }
    }

    /**
     * Splits a connected graph into 2 disconnected segments.
     */
    @Override
    public void splitGraph() {
        int startVertex = 0, finishVertex = 0;
        int countEdges;
        boolean isConnected = false;

        // pick 2 random connected vertices with 3 edges that aren't in the same row or column
        while(!isConnected) {
            do {
                startVertex = getRandomVertex();
                if((countEdges = v[startVertex].countNeighbours()) != 3) {
                    continue;
                }

                finishVertex = getRandomVertex();
                if((countEdges = v[finishVertex].countNeighbours()) != 3) {
                    continue;
                }
            } while (countEdges != 3 ||
                    getCurrentRow(startVertex) == getCurrentRow(finishVertex)
                    || getCurrentColumn(startVertex) == getCurrentColumn(finishVertex));

            Connectivity bfs = new Connectivity(this);
            isConnected = bfs.isConnected(this, startVertex, finishVertex);
        }

        // find the shortest path between the 2 vertices
        Path dijkstry = new Path(this);
        ArrayList<Integer> path = dijkstry.findPath(this, startVertex, finishVertex);
        // for all vertices from the path, cut all right and bottom edges
        for(int i = (path.size() - 1); i > 0; i--) {
            int currentVertex = path.get(i);
            v[currentVertex].removeNeighbour(Vertex.RIGHT);
            if((currentVertex + 1) < getGraphSize()) {
                v[currentVertex + 1].removeNeighbour(1);
            }
            v[currentVertex].removeNeighbour(3);
            if((currentVertex + columns) < getGraphSize()) {
                v[currentVertex + columns].removeNeighbour(0);
            }
        }

        // for all vertices that became entirely disconnected, restore 1 possible edge
        double w = 0;
        for(int i = 0; i < getGraphSize(); i++) {
            if(v[i].countNeighbours() == 0) {
                if(getCurrentRow(i) != 0) {
                    w = getRandomWeight();
                    v[i].setNeighbour(Vertex.UPPER, (i - columns), w);
                    v[i - columns].setNeighbour(Vertex.LOWER, i, w);
                } else if(getCurrentColumn(i) != 0) {
                    w = getRandomWeight();
                    v[i].setNeighbour(Vertex.LEFT, (i - 1), w);
                    v[i - 1].setNeighbour(Vertex.RIGHT, i, w);
                } else if(getCurrentColumn(i) != (columns - 1)) {
                    w = getRandomWeight();
                    v[i].setNeighbour(Vertex.RIGHT, (i + 1), w);
                    v[i + 1].setNeighbour(Vertex.LEFT, i, w);
                }
                else if(getCurrentRow(i) != (rows - 1)) {
                    v[i].setNeighbour(Vertex.LOWER, (i + columns), w);
                    v[i + columns].setNeighbour(Vertex.UPPER, i, w);
                }
            }
        }
    }

    /**
     * Adds a neighbour of an unknown position to a vertex.
     * @param vertex index of the main vertex
     * @param neighbour index of the neighbouring vertex
     * @param weight weight of the edge between the 2 vertices
     * @return error code (0 - success, -1 - failure)
     */
    private int addNeighbour(int vertex, int neighbour, double weight) {
        if (neighbour < 0 || neighbour >= getGraphSize() || neighbour == vertex || weight <= 0) {
            return -1;
        }
        if (neighbour == (vertex - columns)) {
            v[vertex].setNeighbour(Vertex.UPPER, neighbour, weight);
            return 0;
        }
        if (neighbour == (vertex - 1)) {
            v[vertex].setNeighbour(Vertex.LEFT, neighbour, weight);
            return 0;
        }
        if (neighbour == (vertex + 1)) {
            v[vertex].setNeighbour(Vertex.RIGHT, neighbour, weight);
            return 0;
        }
        if (neighbour == (vertex + columns)) {
            v[vertex].setNeighbour(Vertex.LOWER, neighbour, weight);
            return 0;
        }
        return -1;
    }

    /**
     * Reads a graph from a file.
     * @param reader provided FileReader
     * @throws IOException in case of an incorrect file format
     */
    @Override
    public void readGraph(FileReader reader) throws IOException {
        try {
            BufferedReader buffer = new BufferedReader(reader);
            String[] line = buffer.readLine().trim().split("\\s");
            rows = Integer.parseInt(line[0]);
            columns = Integer.parseInt(line[1]);

            if (rows * columns > 10000 || rows * columns <= 0) {
                // change to an exception? (arguments out of range or incorrect file format)
                System.out.println("Error");
            } else {
                v = new Vertex[rows * columns];
                for (int i = 0; i < getGraphSize(); i++)
                    v[i] = new Vertex();

                int neighbour;
                double weight;
                for (int i = 0; i < getGraphSize(); i++) {
                    line = buffer.readLine().trim().split("[\\s:]+");
                    for (int j = 0; (j + 1) < line.length; j++) {
                        neighbour = Integer.parseInt(line[j]);
                        weight = Double.parseDouble(line[++j]);
                        if (addNeighbour(i, neighbour, weight) == -1) {
                            // change to an exception? arguments out of range or incorrect file format
                            System.out.println("Error");
                        }
                    }
                    setWeightRange();
                }
            }
        } catch(NumberFormatException e) {
            throw new IOException("Incorrect file format.");
        }
    }

    /**
     * Writes the graph into a file.
     * @param writer provided PrintWriter
     */
    @Override
    public void writeGraph(PrintWriter writer) {
        writer.println(rows + " " + columns);
        for(Vertex current : v) {
            writer.println("\t" + current);
        }
        writer.close();
    }

    /**
     * Prints the graph contents to standard output.
     */
    @Override
    public void printGraph() {
        System.out.println("rows = " + rows + " columns = " + columns);
        System.out.println("w1 = " + weightLower + " w2 = " + weightUpper);
        for(Vertex current : v) {
            System.out.println(current);
        }
    }
}