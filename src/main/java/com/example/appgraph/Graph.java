package com.example.appgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Graph implements GeneratedGraph, ReadGraph {
    private int rows;
    private int columns;
    private Vertex [] v;
    private double weightLower;
    private double weightUpper;
    private final Random r = new Random();

    public Graph(int rows, int columns, int w1, int w2) {
        // generated randomly
        this.rows = rows;
        this.columns = columns;
        this.weightLower = w1;
        this.weightUpper = w2;
        v = new Vertex[rows * columns];
        for(int i = 0; i < getGraphSize(); i++)
            v[i] = new Vertex(i);
    }

    public Graph() {
        // creates an empty graph to be read from a file later
        rows = columns = 0;
        weightLower = Double.POSITIVE_INFINITY;
        weightUpper = -1;
    }

    @Override
    public int getGraphSize() {
        return rows * columns;
    }

    private int getCurrentRow(int index) {
        return index / columns;
    }

    private int getCurrentColumn(int index) {
        return index % columns;
    }

    private double getRandomWeight() {
        return weightLower + (weightUpper - weightLower) * r.nextDouble();
    }

    //
    @Override
    public  int getRows() {return  rows;}

    @Override
    public int getColumns() {return columns;}

    @Override
    public Vertex getVertex(int index) {
        return v[index];
    }

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

    @Override
    public void splitGraph() {
        // to be implemented
    }

    @Override
    public void readGraph(FileReader reader) throws IOException {
        try {
            BufferedReader buffer = new BufferedReader(reader);
            String[] line = buffer.readLine().trim().split("\\s");
            rows = Integer.parseInt(line[0]);
            columns = Integer.parseInt(line[1]);

            if (rows * columns > 1000000 || rows * columns <= 0) {
                // change to an exception? (arguments out of range or incorrect file format)
                System.out.println("Error");
            } else {
                v = new Vertex[rows * columns];
                for (int i = 0; i < getGraphSize(); i++)
                    v[i] = new Vertex(i);

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

    @Override
    public void writeGraph(PrintWriter writer) {
        writer.println(rows + " " + columns);
        for(Vertex current : v) {
            writer.println("\t" + current);
        }
        writer.close();
    }

    @Override
    public void printGraph() {
        System.out.println("rows = " + rows + " columns = " + columns);
        System.out.println("w1 = " + weightLower + " w2 = " + weightUpper);
        for(Vertex current : v) {
            System.out.println(current);
        }
    }
}
