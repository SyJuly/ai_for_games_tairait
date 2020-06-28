package AI;

import AI.Bot;
import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty(BotManager botManager) {
        super(botManager,1, 0);
    }


    @Override
    public void findNextPath(List<Point> allEnemiesPoints) {
        if(allEnemiesPoints.size() < 1){
            findRandomPath();
            return;
        }
        List<List<Point>> clusters = botManager.getClusterer().cluster(allEnemiesPoints);
        if(clusters.size() < 1){
            findRandomPath();
            return;
        }
        List<Point> nearestCluster = null;
        float minDistance = Float.POSITIVE_INFINITY;
        for(int i = 0; i < clusters.size(); i++){
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

        Comparator<Point> comparator = new NearestPointComparator(x,y);
        Collections.sort(nearestCluster, comparator);
        Point target = nearestCluster.get(nearestCluster.size() - 1);

        int[][] path = botManager.getPathFinder().AStarSearch((int)x, (int)y,target.x,target.y, false);
        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }

}
