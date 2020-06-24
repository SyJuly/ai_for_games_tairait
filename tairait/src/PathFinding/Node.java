package PathFinding;

import java.util.ArrayList;
import java.util.List;

public class Node{
    public int[] pos;
    public List<Edge> adjacency = new ArrayList<>();
    public Node parent;
    public double g_cost = Integer.MAX_VALUE;
    public double f_cost = Integer.MAX_VALUE;
    public double h_cost;

    public Node(int x, int y){
        pos = new int[]{x,y};
    }

    public void addNeighbour(Node neighbour, int cost) {
        Edge edge = new Edge(neighbour, cost);
        adjacency.add(edge);
    }

    public String toString(){
        return "N(" + pos[0] + "," + pos[1] + "|E:" + adjacency.size() + ")";
    }

    public boolean isPosition(int[] position){
        return pos[0] == position[0] && pos[1] == position[1];
    }

    public void reset(){
        parent = null;
        g_cost = Integer.MAX_VALUE;
        f_cost = Integer.MAX_VALUE;
        h_cost = 0.0;
    }

}
