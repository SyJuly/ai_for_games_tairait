package PathFinding;

public class Edge{
    public double cost;
    public double avoidCenter_cost;
    public double preference_cost;
    public Node target;

    public Edge(Node target, double cost, double preference_cost){
        this.target = target;
        this.cost = cost;
        this.preference_cost = preference_cost;

    }
    public Edge(Node target, double cost){
        this.target = target;
        this.cost = cost;

    }

}