package Module.graph;


import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class EdgeFunctions<V> {

    protected ArrayList<V> vertices = new ArrayList<>();
    protected ArrayList<ArrayList<Edge>> neighbors = new ArrayList<>();



    /** Construct an empty graph */
    public EdgeFunctions() {
    }

    private void createAdjacencyLists(int[][] edges) {

        for (int i = 0; i < edges.length; i++) {
            double weigth = edges[i][2];
            addEdge(edges[i][0], edges[i][1], weigth);
        }
    }


    public EdgeFunctions(List<V> vertices, ArrayList<Edge> edges) {

        for (V v : vertices) {

            addVertex(v);
        }

        createAdjacencyLists(edges);

    }

    private void createAdjacencyLists(ArrayList<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge.getVertixFrom(), edge.getVertixTo(), edge.getWeight());
        }
    }

    public boolean addEdge(int u, int v, double weight) {
        Edge edge = new Edge(u, v, weight);
        return addEdge(edge);
    }

    public boolean addEdge(Edge e) {

        if (e.getVertixFrom() < 0 || e.getVertixFrom() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getVertixFrom());

        if (e.getVertixTo() < 0 || e.getVertixTo() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getVertixTo());

        if (!neighbors.get(e.getVertixFrom()).contains(e)) {
            neighbors.get(e.getVertixFrom()).add(e);
            return true;
        }

        return false;
    }

    public int getSize() {
        return vertices.size();
    }



    public V getVertex(int index) {
        return vertices.get(index);
    }




    public boolean addVertex(V vertex) {

        if (!vertices.contains(vertex)) {

            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;

        }

        return false;
    }


    private Node[] getShortestPaths(int source) {

        Node[] nodes = getNodes(source);

        PriorityQueue<Node> vertices = new PriorityQueue<>();
        vertices.add(nodes[source]);

        while (!vertices.isEmpty()) {

            Node node = vertices.poll();
            ArrayList<Edge> adjacentVertices = this.neighbors.get(node.getVertix());

            for (Edge edge : adjacentVertices) {

                int adjacentVertix = edge.getVertixTo();
                double weigth = edge.getWeight();

                double totalCost = node.getCost() + weigth;

                if (nodes[adjacentVertix].getCost() > totalCost) {

                    nodes[adjacentVertix].setCost(totalCost);
                    nodes[adjacentVertix].setParentVertix(node.getVertix());
                    vertices.add(nodes[adjacentVertix]);
                }
            }
        }

        return nodes;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * This method will get String that contains shortest path from givern source to
     * given destination depend on array of nodes.
     *
     */
    public Stack<V> getShortestPath(int source, int destination) {

        if (source < 0 || source > vertices.size() - 1) {
            throw new IllegalArgumentException("Out of Bound");
        }

        if (destination < 0 || destination > vertices.size() - 1) {
            throw new IllegalArgumentException("Out of Bound");
        }

        Node[] nodes = getShortestPaths(source);

        int current = destination;
        Stack<V> shortestPath = new Stack<>();
        int counter = 0;
        while (current != -1) {

            Node node = nodes[current];
            shortestPath.add(getVertex(node.getVertix()));
            current = node.getParentVertix();
            counter++;

        }

        return (counter > 1) ? shortestPath : null;

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * This method will build and initialize a table that will used to solve
     * Dijikstra Algorithm for a given source
     */
    private Node[] getNodes(int source) {

        Node[] nodes = new Node[vertices.size()];
        for (int i = 0; i < nodes.length; i++) {

            nodes[i] = new Node(i);

        }

        nodes[source].setCost(0.0);

        return nodes;

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Inner Class That represent The Unit of Dijikstra Table
     *
     * @author HP
     *
     */
    public static class Node implements Comparable<Node> {

        private double cost = Double.MAX_VALUE;
        private int vertix;
        private int parentVertix = -1;

        public Node(int vertix) {

            this.vertix = vertix;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public int getVertix() {
            return vertix;
        }

        public void setVertix(int vertix) {
            this.vertix = vertix;
        }

        public int getParentVertix() {
            return parentVertix;
        }

        public void setParentVertix(int parentVertix) {
            this.parentVertix = parentVertix;
        }

        @Override
        public int compareTo(Node o) {

            if (this.cost > o.cost) {


                return 1;

            } else if (this.cost < o.cost) {

                return -1;

            } else {
                return 0;

            }
        }

    }
}
