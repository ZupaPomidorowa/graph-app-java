package com.example.appgraph;

import java.util.ArrayList;
import java.util.Arrays;

public class Path {
    int [] predecessor;
    double [] distance;

    private static class PriorityQueue {
        int [] heap;
        int count;
        double [] distance;
        int [] position;

        PriorityQueue(double [] distance) {
            this.distance = distance;
            this.heap = new int [distance.length];
            this.count = 0;
            this.position = new int [distance.length];
            Arrays.fill(this.position, -1);
        }

        void heapUp(int child) {
            int parent, tmp;

            while(child > 0) {
                parent = (child - 1) / 2;

                if(distance[heap[parent]] <= distance[heap[child]]) {
                    return;
                }

                tmp = heap[parent];
                heap[parent] = heap[child];
                heap[child] = tmp;

                position[heap[parent]] = parent;
                position[heap[child]] = child;

                child = parent;
            }
        }

        void heapDown() {
            int parent = 0, tmp;
            int child = 1;

            while(child < count) {
                if (((child + 1) < count) && (distance[heap[child + 1]] < distance[heap[child]])) {
                    child++;
                }
                if (distance[heap[parent]] <= distance[heap[child]]) {
                    return;
                }

                tmp = heap[parent];
                heap[parent] = heap[child];
                heap[child] = tmp;

                position[heap[parent]] = parent;
                position[heap[child]] = child;

                parent = child;
                child = 2 * parent + 1;
            }
        }

        void push(int vertex, double distance) {
            this.distance[vertex] = distance;
            if(position[vertex] == -1) {
                heap[count] = vertex;
                position[vertex] = count;
                count++;
            }
            heapUp(position[vertex]);
        }

        int pop() {
            int popped = heap[0];
            count--;
            heap[0] = heap[count];
            position[popped] = -1;
            position[heap[0]] = 0;
            heapDown();
            return popped;
        }

        boolean isEmpty() {
            return (count == 0);
        }
    }

    public Path(Graph g) {
        this.predecessor = new int [g.getGraphSize()];
        Arrays.fill(this.predecessor, -1);
        this.distance = new double [g.getGraphSize()];
        Arrays.fill(this.distance, Double.POSITIVE_INFINITY);
    }

    private void dijkstry(Graph g, int startVertex) {
        PriorityQueue pq = new PriorityQueue(distance);

        distance[startVertex] = 0.0;
        pq.push(startVertex, distance[startVertex]);

        while(!pq.isEmpty()) {
            int currentVertex = pq.pop();
            for(int i = 0; i < 4; i++) {
                int neighbour = g.getVertex(currentVertex).getNeighbour(i);
                if(neighbour != -1) {
                    double newDistance = distance[currentVertex] + g.getVertex(currentVertex).getWeight(i);
                    if(distance[neighbour] > newDistance) {
                        distance[neighbour] = newDistance;
                        predecessor[neighbour] = currentVertex;
                        pq.push(neighbour, distance[neighbour]);
                    }
                }
            }
        }
    }

    public ArrayList<Integer> findPath(Graph g, int startVertex, int finishVertex) {
        dijkstry(g, startVertex);

        if(predecessor[finishVertex] == -1) {
            // path doesn't exist
            return null;
        }

        ArrayList<Integer> path = new ArrayList<>();
        int currentVertex = finishVertex;
        while(currentVertex != startVertex) {
            path.add(currentVertex);
            currentVertex = predecessor[currentVertex];
        }
        path.add(currentVertex);
        return path;
    }
}
