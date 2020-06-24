package AI;

import Board.BoardManager;
import Board.Point;
import PathFinding.AStar;

import java.util.*;

public class Clusterer {

    private final int NOISE = 0;
    private final int PART_OF_CLUSTER = 1;

    private BoardManager boardManager;

    int minPts = 4;

    public Clusterer(BoardManager boardManager){
        this.boardManager = boardManager;
    }

    public List<List<Point>> cluster(List<Point> points) {
        System.out.println("Number of points: " + points.size());
        List<List<Point>> clusters = new ArrayList<>();
        Map<Point, Integer> visited = new HashMap<>();

        for (Point point : points) {
            if (visited.containsKey(point)) {
                continue;
            }
            List<Point> neighbours = getNeighbors(point, points);
            if (neighbours.size() >= minPts) {
                // DBSCAN does not care about center points
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
                        if (!seeds.contains(point)) {
                            seeds.add(p);
                        }
                    }
                }
            }

            if (visited.containsKey(current) && visited.get(current) != PART_OF_CLUSTER) {
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
