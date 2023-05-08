package Module.graph;

public class Edge {

    private int vertixFrom;

    private int vertixTo;

    private double weight;

    public Edge(int vertixFrom, int vertixTo, double weight) {

        this(vertixFrom,vertixTo);
        this.weight = weight;
    }

    public Edge(int vertixFrom, int vertixTo) {

        this.vertixFrom = vertixFrom;
        this.vertixTo = vertixTo;
    }

    public int getVertixFrom() {
        return vertixFrom;
    }


    public int getVertixTo() {
        return vertixTo;
    }


    public double getWeight() {
        return weight;
    }


    @Override
    public boolean equals(Object o) {

        if (o == null) {

            return false;
        }

        if (this == o) {
            return true;
        }

        if (o.getClass() == this.getClass()) {

            Edge edge = (Edge) o;
            return (this.vertixFrom == edge.vertixFrom && this.vertixTo == edge.vertixTo) ? true : false;

        }

        return false;
    }
}
