package pathFinding;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
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
        return "N(" + pos[0] + "|" + pos[1] +")";
    }

    public boolean isPosition(int[] position){
        return pos[0] == position[0] && pos[1] == position[1];
    }

    @Override
    public int compareTo(Node otherNode) {
        return Double.compare(g_cost, otherNode.g_cost);
    }
}
