package PathFinding;

public class Edge{
    public double cost;
    public double avoidCenter_cost;
    public double avoidOwnField_cost;
    public Node target;

    public Edge(Node target, double cost, double avoidCenter_cost){
        this.target = target;
        this.cost = cost;
        this.avoidCenter_cost = avoidCenter_cost;

    }
    public Edge(Node target, double cost){
        this.target = target;
        this.cost = cost;

    }

}