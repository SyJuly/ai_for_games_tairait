package PathFinding;

public class Edge{
    public double cost;
    public Node target;

    public Edge(Node target, double cost){
        this.target = target;
        this.cost = cost;

    }
}