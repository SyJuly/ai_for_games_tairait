package pathFinding;

import java.util.*;

public class AStar {

    Node n0 = new Node(0,0);
    Node n1 = new Node(0,1);
    Node n2 = new Node(1,0);
    Node n3 = new Node(1,1);
    Node n4 = new Node(0,2);
    Node n5 = new Node(2,0);
    Node n6 = new Node(1,2);
    Node n7 = new Node(2,1);
    Node n8 = new Node(2,2);

    AStar(){
        n0.adjacency.add(new Edge(n1,1));
        n0.adjacency.add(new Edge(n2,1));
        n1.adjacency.add(new Edge(n4,1));
        n1.adjacency.add(new Edge(n3,1));
        n2.adjacency.add(new Edge(n3,1));
        n2.adjacency.add(new Edge(n5,1));
        n3.adjacency.add(new Edge(n6,1));
        n6.adjacency.add(new Edge(n8,1));
        n5.adjacency.add(new Edge(n7,1));
        System.out.println(Arrays.toString(AStarSearch(n3, n8)));

    }

    public Node[] AStarSearch(Node start, Node target) {

        start.g_cost = 0;
        start.f_cost = start.h_cost;
        PriorityQueue<Node> open = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();

        open.add(start);
        int numberOfSteps = 0;
        while (!open.isEmpty()) {

            numberOfSteps++;
            Node current = open.poll();

            // we have found the target
            if (current.isPosition(target.pos)) {
                break;
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            // check all the neighbours

            for (Edge edge: current.adjacency) {
                Node neighourNode = edge.target;
                double cost = edge.cost;

                double new_g_cost = current.g_cost + cost;
                double new_f_cost = neighourNode.h_cost + new_g_cost;

                if (new_f_cost < neighourNode.f_cost) {
                    neighourNode.parent = current;
                    System.out.println("setting parent " + current.toString() + " to node " + neighourNode.toString());
                    neighourNode.g_cost = new_g_cost;
                    neighourNode.f_cost = new_f_cost;

                    if (open.contains(neighourNode)) {
                        open.remove(neighourNode);
                    }

                    open.add(neighourNode);
                }
            }
        }
        System.out.println("Number of steps: " + numberOfSteps);

        List<Node> path = new ArrayList<>();
        Node next = target;
        while (!next.isPosition(start.pos)) {
            path.add(next);
            next = next.parent;
        }
        path.add(start);
        Collections.reverse(path);
        return path.toArray(new Node[path.size()]);
    }

    public static void main(String[] args) {
        new AStar();
    }
}
