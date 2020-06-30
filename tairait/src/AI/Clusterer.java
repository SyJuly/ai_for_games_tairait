package AI;

import Board.BoardManager;
import Board.Point;
import PathFinding.AStar;

import java.util.*;

public class Clusterer {

    private final int NOISE = 0;
    private final int PART_OF_CLUSTER = 1;

    private BoardManager boardManager;

    int minPts = 2;

    public Clusterer(BoardManager boardManager){
        this.boardManager = boardManager;
    }

    public List<List<Point>> cluster(List<Point> points) {
        List<List<Point>> clusters = new ArrayList<>();
        Map<Point, Integer> visited = new HashMap<>();

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (visited.containsKey(point)) {
                continue;
            }
            List<Point> neighbours = getNeighbors(point, points);
            if (neighbours.size() >= minPts) {
                List<Point> cluster = new ArrayList<>();
                clusters.add(expandCluster(cluster, point, neighbours, points, visited));
            } else {
                visited.put(point, NOISE);
            }
        }

        return clusters;
    }

    private List<Point> expandCluster(List<Point> cluster,
                                      Point point,
                                      List<Point> neighbors,
                                      List<Point> points,
                                      Map<Point, Integer> visited) {
        cluster.add(point);
        visited.put(point, PART_OF_CLUSTER);

        List<Point> seeds = new ArrayList<>(neighbors);
        int index = 0;
        while (index < seeds.size()) {
            Point current = seeds.get(index);
            // only check non-visited points
            if (!visited.containsKey(current)) {
                List<Point> currentNeighbors = getNeighbors(current, points);
                if (currentNeighbors.size() >= minPts) {
                    for (Point p : currentNeighbors) {
                        if (!seeds.contains(p)) {
                            seeds.add(p);
                        }
                    }
                }
            }

            if (!visited.containsKey(current) || (visited.containsKey(current) && visited.get(current) != PART_OF_CLUSTER)) {
                visited.put(current, PART_OF_CLUSTER);
                cluster.add(current);
            }

            index++;
        }
        return cluster;
    }

    private List<Point> getNeighbors(Point point, List<Point> points){
        List<Point> neighboursOfPoints = new ArrayList<>();
        for(int n = 0; n < AStar.NEIGHBOURS.length; n++){
            int neighbour[] = AStar.NEIGHBOURS[n];
            int neighbourX =point.x + neighbour[0];
            int neighbourY =point.y + neighbour[1];
            if((neighbourX > boardManager.WORLD_SIZE - 1 || neighbourX < 0)||
               (neighbourY > boardManager.WORLD_SIZE - 1 || neighbourY < 0)){
            continue;
            }
            Point neighourPoint = boardManager.getBoard()[point.x + neighbour[0]][point.y + neighbour[1]];
            if(points.contains(neighourPoint)){
                neighboursOfPoints.add(neighourPoint);
            }
        }
        return neighboursOfPoints;
    }

    public void printCluster(List<List<Point>> clusters){
        System.out.println(".................................");
        for(int n = 0; n < clusters.size(); n++){
            System.out.println("Cluster " + n + "\n");
            System.out.println(Arrays.toString(clusters.get(n).toArray()));
            System.out.println("\n");
        }

        System.out.println(".................................");
    }

}
