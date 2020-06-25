package PathFinding;

import Board.Point;

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

    public final static int NEIGHBOURS[][] = new int[][]{
            {-1,-1},
            {-1,0},
            {-1, 1},
            {0,1},
            {1,  1},
            {1,0},
            {1, -1},
            {0,-1}};


    private Node[][] nodes;

    public AStar(Point[][] world){
        nodes = new Node[world.length][world[0].length];
        for(int x = 0; x < world.length; x++){
            for(int y = 0; y < world[x].length; y++){
                if(world[x][y].statusCode >= 0){
                    nodes[x][y] = new Node(x,y);
                }
            }
        }
        for(int x = 1; x < nodes.length - 1; x++){
            for(int y = 1; y < nodes[x].length - 1; y++){
                if(nodes[x][y] == null){
                    continue;
                }
                for(int n = 0; n < NEIGHBOURS.length; n++){
                    int neighbour[] = NEIGHBOURS[n];
                    Node neighbourNode = nodes[x + neighbour[0]][y + neighbour[1]];
                    if(neighbourNode != null && !isCriticalDiagonal(n, nodes[x][y])){
                        nodes[x][y].addNeighbour(neighbourNode,1);
                    }
                }
            }
        }
        overrideEdges_preferOuterSpace();
    }

    public void overrideEdges_preferOuterSpace(){
        Point maxPoint = null;
        double maxVal = Double.NEGATIVE_INFINITY;
        double minVal = Double.POSITIVE_INFINITY;
        Point minPoint = null;
        for(int x = 1; x < nodes.length - 1; x++){
            for(int y = 1; y < nodes[x].length - 1; y++){
                Node node = nodes[x][y];
                if(nodes[x][y] == null){
                    continue;
                }
                double eX = (1.0/(0.5*Math.sqrt(2 * Math.PI)))* Math.pow(Math.E, -0.5 * Math.pow((x-15.0)/0.5, 2));
                double eY = (1.0/(0.5*Math.sqrt(2 * Math.PI)))* Math.pow(Math.E, -0.5 * Math.pow((y-15.0)/0.5,2));
                double cost = 1 + eX + eY;
                if(maxPoint == null || maxVal < cost){
                    maxPoint = new Point(x,y);
                    maxVal = cost;
                }
                if(minPoint == null || minVal > cost){
                    minPoint = new Point(x,y);
                    minVal = cost;
                }
                for(int e = 0; e < node.adjacency.size(); e++){
                    node.adjacency.get(e).cost = cost;
                }
            }
        }
        //System.out.println("Max Cost: " + maxVal + " at " + maxPoint);
        //System.out.println("Min Cost: " + minVal + " at " + minPoint);
    }

    private boolean isCriticalDiagonal(int neighbourIndex, Node currentNode) {
        int[] neighbour = NEIGHBOURS[neighbourIndex];
        if(neighbour[0] == 0 || neighbour[1] == 0){
            return false;
        }
        return isNeighbourWall(neighbourIndex + 1, currentNode) || isNeighbourWall(neighbourIndex - 1, currentNode);
    }

    private boolean isNeighbourWall(int neighbourIndex, Node currentNode) {
        int[] neighbour;
        if(neighbourIndex > NEIGHBOURS.length - 1){
            neighbour = NEIGHBOURS[0];
        } else if (neighbourIndex < 0){
            neighbour = NEIGHBOURS[NEIGHBOURS.length - 1];
        } else{
            neighbour = NEIGHBOURS[neighbourIndex];
        }
        return nodes[currentNode.pos[0] + neighbour[0]][currentNode.pos[1] + neighbour[1]] == null;
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
        if(start == null){
            System.out.println("Something went wrong. Start node was null.");
        }
        if(target == null){
            System.out.println("Something went wrong. Target node was null.");
        }

        start.g_cost = 0;
        start.f_cost = start.h_cost;

        PriorityQueue<Node> open = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.f_cost, o2.f_cost);
            }

        });

        HashSet<Node> closed = new HashSet<>();

        open.add(start);
        int numberOfSteps = 0;
        while (!open.isEmpty()) {

            numberOfSteps++;
            Node current = open.poll();

            // we have found the target
            if (current.isPosition(target.pos)) {
                break;

            }

            // check all the NEIGHBOURS

            for (Edge edge: current.adjacency) {
                Node neighourNode = edge.target;

                boolean isOpen = open.contains(neighourNode);
                boolean isClosed = closed.contains(neighourNode);

                double cost = edge.cost;

                double new_g_cost = current.g_cost + cost;
                double new_f_cost = neighourNode.h_cost + new_g_cost;

                if ((!isOpen && !isClosed) && new_f_cost < neighourNode.f_cost) {
                    neighourNode.parent = current;
                    neighourNode.g_cost = new_g_cost;
                    neighourNode.f_cost = new_f_cost;

                    if (isClosed) {
                        closed.remove(neighourNode);
                    }
                    if (!isOpen) {
                        open.add(neighourNode);
                    }
                }
            }
            closed.add(current);
        }
        System.out.println("Number of steps: " + numberOfSteps);

        List<Node> path = new ArrayList<>();
        Node next = target;
        while (!next.isPosition(start.pos)) {
            path.add(next);

            if(next.parent == null){
                System.out.println("Next was null");
                System.out.println("Error: " + Arrays.toString(path.toArray()));
            }
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
