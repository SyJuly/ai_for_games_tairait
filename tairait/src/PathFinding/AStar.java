package PathFinding;

import Board.Point;
import lenz.htw.tiarait.net.NetworkClient;

import java.util.*;

public class AStar {

    public final static int NEIGHBOURS[][] = new int[][]{
            {-1,-1},
            {-1,0},
            {-1, 1},
            {0,1},
            {1,  1},
            {1,0},
            {1, -1},
            {0,-1}};

    private final static int DEFAULT_COST = 10;
    private final static int OWNER_COST = 2000000;
    private final static int ENEMY_COST = -7;
    private Node[][] nodes;
    private int ownTeamCode;

    private double normalDistributionFactorA =(1.0/(4.453*Math.sqrt(2 * Math.PI)));

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
                double eX = normalDistributionFactorA* Math.pow(Math.E, -0.5 * Math.pow((x-15.5)/4.453, 2));
                double eY = normalDistributionFactorA* Math.pow(Math.E, -0.5 * Math.pow((y-15.5)/4.453,2));
                double avoidCenter_cost = (1 + eX + eY) * 100;
                //System.out.println(x+"," +y + "--cost:" +avoidCenter_cost);
                for(int n = 0; n < NEIGHBOURS.length; n++){
                    int neighbour[] = NEIGHBOURS[n];
                    Node neighbourNode = nodes[x + neighbour[0]][y + neighbour[1]];
                    if(neighbourNode != null && !isCriticalDiagonal(n, nodes[x][y])){
                        nodes[x][y].addNeighbour(neighbourNode,DEFAULT_COST, avoidCenter_cost);
                    }
                }
            }
        }
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

    public int[][] AStarSearch(int startX, int startY, int targetX, int targetY, Point[][] board, float[][] bots, boolean avoidCenter, int botCode) {
        if(board[targetX][targetY].statusCode == ownTeamCode){
            System.out.println("Something went wrong. Set owned point as target!!!!!!!!!!!!!!!!!!!!! It was bot " + botCode);
        }
        prepareGraph(board, bots, ownTeamCode, botCode);
        List<Node> nodePath = AStarSearch(nodes[startX][startY], nodes[targetX][targetY], avoidCenter, botCode);
        if(nodePath == null){
            return null;
        }
        int[][] path = new int[nodePath.size()][2];

        for(int i = 0; i < nodePath.size(); i++){
            Node node = nodePath.get(i);
            path[i] = new int[]{node.pos[0], node.pos[1]};
        }
        return path;
    }

    public List<Node> AStarSearch(Node start, Node target, boolean avoidCenter, int owner) {
        if(start == null){
            System.out.println("Something went wrong. Start node was null.");
            return null;
        }
        if(target == null){
            System.out.println("Something went wrong. Target node was null");
            return null;
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

                double cost = edge.cost + edge.preference_cost;
                if(avoidCenter){
                    cost += edge.avoidCenter_cost;
                }
                if(neighourNode.markedAsToBeOwnedBy >= 0){
                    cost += OWNER_COST;
                }

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
        //System.out.println("Number of steps: " + numberOfSteps);

        int pathLeadingOverOwnTeam = 0;
        List<Node> path = new ArrayList<>();
        Node next = target;
        while (!next.isPosition(start.pos)) {
            if(next.markedAsToBeOwnedBy > 0){
                pathLeadingOverOwnTeam++;
            }
            markOwnership(owner, next);
            path.add(next);

            Edge nextEdge = getEdge(next, next.parent);
            if(nextEdge != null && nextEdge.preference_cost == OWNER_COST){
                pathLeadingOverOwnTeam++;
            }
            next = next.parent;
            if(next == null){ // target was not reachable
                return null;
            }
        }
        path.add(start);
        Collections.reverse(path);
        //System.out.println("Times path leads over own point: " + pathLeadingOverOwnTeam);
        //System.out.println(Arrays.toString(path.toArray()));
        return path;
    }

    private void markOwnership(int owner, Node next) {
        next.markedAsToBeOwnedBy = owner;
        for(Edge edge : next.adjacency){
            if(edge.target.pos[0] == next.pos[0] || edge.target.pos[1] == next.pos[1]){
                edge.target.markedAsToBeOwnedBy = owner;
            }
        }
    }

    private Edge getEdge(Node nodeTo, Node nodeFrom) {
        if(nodeFrom == null | nodeTo == null){
            return null;
        }
        for(int i = 0; i < nodeFrom.adjacency.size(); i++){
            if(nodeFrom.adjacency.get(i).target == nodeTo){
                return nodeFrom.adjacency.get(i);
            }
        }
        return null;
    }


    public void prepareGraph(Point[][] board, float[][] bots, int ownTeamCode, int botOwner) {
        //Set<Node> targets = new HashSet<>();
        for(int x = 0; x < nodes.length; x++){
            for(int y = 0; y < nodes[x].length; y++){
                Node node = nodes[x][y];
                if(node == null){
                    continue;
                }
                nodes[x][y].reset(botOwner);
                for(int e = 0; e < node.adjacency.size(); e++){
                    Edge edge = node.adjacency.get(e);
                    int targetX = edge.target.pos[0];
                    int targetY = edge.target.pos[1];
                    int statusCode = board[targetX][targetY].statusCode;
                    int distanceCost = 0;

                    for(int b = 0; b < bots.length; b++){
                        double distance = Math.sqrt((targetY - bots[b][1]) * (targetY - bots[b][1]) + (targetX - bots[b][0]) * (targetX - bots[b][0]));
                        if(distance < 3){
                            distanceCost++;
                        }
                    }

                    if(statusCode == ownTeamCode){
                        edge.preference_cost = OWNER_COST;
                        //targets.add(edge.target);
                    } else if(statusCode > 0){
                        edge.preference_cost = ENEMY_COST;
                    } else if(statusCode == 0){
                        edge.preference_cost = 0;
                    }
                    edge.preference_cost += distanceCost;
                }
            }
        }
        //System.out.println("AStar marked: " + targets.size() + " as owned.");
    }

    public void releasePredictedOwnership(int x, int y, int owner){
        Node node = nodes[x][y];
        if(node.markedAsToBeOwnedBy == owner){
            node.markedAsToBeOwnedBy = -1;
        }
    }


    private Node getNode(int x, int y){
        if(nodes[x][y] == null){
            System.out.println("Node was null: " + x + "," + y);
        }
        return nodes[x][y];
    }

    public void setOwnTeamCode(int ownTeamCode){
        this.ownTeamCode = ownTeamCode;
    }
}
