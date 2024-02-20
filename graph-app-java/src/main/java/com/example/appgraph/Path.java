package com.example.appgraph;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for finding the shortest path between vertices using the Dijkstry algorithm.
 */
public class Path {
    /**
     * Array of predecessors for each vertex.
     */
    int [] predecessor;

    /**
     * Array of distances to each vertex from the start vertex.
     */
    double [] distance;

    /**
     * Priority queue implementing a heap (distance as a priority factor for vertices).
     */
    private static class PriorityQueue {
        /**
         * Array implementing a heap, stores vertices' indexes.
         */
        int [] heap;

        /**
         * Number of vertices currently on the heap.
         */
        int count;

        /**
         * Array of distances to each vertex from the start vertex (priority factor).
         */
        double [] distance;

        /**
         * Array of each vertex's current index (position) in the heap array.
         * (-1) if not on the heap.
         */
        int [] position;

        /**
         * Builds a priority queue based on the length of the existing distance array.
         * @param distance existing distance array of (rows * columns) length
         */
        PriorityQueue(double [] distance) {
            this.distance = distance;
            this.heap = new int [distance.length];
            this.count = 0;
            this.position = new int [distance.length];
            Arrays.fill(this.position, -1);
        }

        /**
         * Orders the heap from the bottom up.
         * Used while adding a new element to the heap or changing an elements' priority.
         * @param child index of the new (or updated) vertex in the heap
         */
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

        /**
         * Orders the heap from the root down.
         * Used when removing an element from the heap.
         */
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

        /**
         * Adds a new vertex to the heap or updates the position of a vertex already on the heap.
         * @param vertex index of the added (or updated) vertex
         * @param distance distance of the added (or updated) vertex from the start vertex
         */
        void push(int vertex, double distance) {
            this.distance[vertex] = distance;
            if(position[vertex] == -1) {
                heap[count] = vertex;
                position[vertex] = count;
                count++;
            }
            heapUp(position[vertex]);
        }

        /**
         * Removes a vertex of the highest priority from the heap.
         * @return the removed vertex
         */
        int pop() {
            int popped = heap[0];
            count--;
            heap[0] = heap[count];
            position[popped] = -1;
            position[heap[0]] = 0;
            heapDown();
            return popped;
        }

        /**
         * Checks if the priority queue is empty.
         * @return true or false
         */
        boolean isEmpty() {
            return (count == 0);
        }
    }

    /**
     * Initially makes empty predecessor and distance arrays.
     * @param g graph in which we want to find the shortest path
     */
    public Path(Graph g) {
        this.predecessor = new int [g.getGraphSize()];
        Arrays.fill(this.predecessor, -1);
        this.distance = new double [g.getGraphSize()];
        Arrays.fill(this.distance, Double.POSITIVE_INFINITY);
    }

    /**
     * Performs the Dijkstry algorithm on a given graph, saves the results in the predecessor and distance arrays.
     * @param g graph
     * @param startVertex vertex from which the path starts
     */
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

    /**
     * Finds the shortest path between 2 chosen vertices.
     * @param g graph
     * @param startVertex vertex from which the path starts
     * @param finishVertex vertex to which the path leads
     * @return ArrayList with the path saved (finish vertex as the 0th element, start vertex as the last element),
     * null if the path doesn't exist
     */
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
