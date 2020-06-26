package AI;

import AI.Bot;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Collections;
import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty() {
        super(1, 1);
    }


    @Override
    public void findNextPath(Clusterer clusterer,
                             AStar pathFinder,
                             List<Point> allEnemiesPoints) {
        List<List<Point>> clusters = clusterer.cluster(allEnemiesPoints);
        List<Point> nearestCluster = null;
        float minDistance = Float.POSITIVE_INFINITY;
        for(int i = 1; i < clusters.size(); i++){
            List<Point> cluster = clusters.get(i);
            for(int j = 0; j < cluster.size(); j++){
                Point p = cluster.get(j);
                float distance = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
                if(nearestCluster == null || distance < minDistance){
                    minDistance = distance;
                    nearestCluster = cluster;
                }
            }
        }

        Collections.sort(nearestCluster, new NearestPointComparator(x,y));
        Point target = nearestCluster.get(nearestCluster.size() - 1);

        int[][] path = pathFinder.AStarSearch((int)x, (int)y,target.x,target.y, false);
        setPath(path);
    }

}
