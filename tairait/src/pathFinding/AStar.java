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

    private final int neighbours[][] = new int[][]{
            {-1,-1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0,  1},
            {1, -1},
            {1,  0},
            {1,  1}};

    private Node[][] nodes;

    public AStar(int[][] world){
        nodes = new Node[world.length][world[0].length];
        for(int x = 0; x < world.length; x++){
            for(int y = 0; y < world[x].length; y++){
                if(world[x][y] >= 0){
                    nodes[x][y] = new Node(x,y);
                }
            }
        }
        for(int x = 1; x < nodes.length - 1; x++){
            for(int y = 1; y < nodes[x].length - 1; y++){
                if(nodes[x][y] == null){
                    continue;
                }
                for(int n = 0; n < neighbours.length; n++){
                    int neighbour[] = neighbours[n];
                    Node neighbourNode = nodes[x + neighbour[0]][y + neighbour[1]];
                    if(neighbourNode != null){
                        nodes[x][y].adjacency.add(new Edge(neighbourNode,1));
                    }

                }
            }
        }
    }

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
        //System.out.println(Arrays.toString(AStarSearch(n3, n8)));

    }

    public int[][] AStarSearch(int startX, int startY, int targetX, int targetY) {
        resetGraph();
        List<Node> nodePath = AStarSearch(nodes[startX][startY], nodes[targetX][targetY]);
        int[][] path = new int[nodePath.size()][2];

        for(int i = 0; i < nodePath.size(); i++){
            Node node = nodePath.get(i);
            path[i] = new int[]{node.pos[0], node.pos[1]};
        }
        return path;
    }

    private void resetGraph() {
        for(int x = 0; x < nodes.length; x++){
            for(int y = 0; y < nodes[x].length; y++){
                if(nodes[x][y] == null){
                    continue;
                }
                nodes[x][y].reset();
            }
        }
    }

    public List<Node> AStarSearch(Node start, Node target) {
        if(start == null || target == null){
            System.out.println("Something went wrong. Start or target node was null.");
        }
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
        System.out.println(Arrays.toString(path.toArray()));
        return path;
    }

    public static void main(String[] args) {
        new AStar();
    }
}
