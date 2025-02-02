package AI;

import AI.Bot;
import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty(BotManager botManager, BotManagerAssistent assistent) {
        super(botManager, assistent,1, 0);
    }


    @Override
    public void findNextPath(List<Point> allEnemiesPoints) {
        Comparator<Point> comparator = new NearestPointComparator(x, y);

        if(allEnemiesPoints.size() < 1){
            return;
        }
        List<List<Point>> clusters = assistent.getPossessedPointClusters();
        Point target;

        if(clusters.size() < 1){
            target = getClosestTarget(allEnemiesPoints, comparator);
        } else {
            List<Point> nearestCluster = null;
            float minDistance = Float.POSITIVE_INFINITY;
            for (int i = 0; i < clusters.size(); i++) {
                List<Point> cluster = clusters.get(i);
                for (int j = 0; j < cluster.size(); j++) {
                    Point p = cluster.get(j);
                    float distance = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
                    if (nearestCluster == null || distance < minDistance) {
                        minDistance = distance;
                        nearestCluster = cluster;
                    }
                }
            }


            Collections.sort(nearestCluster, comparator);
            //System.out.println("Position: "+ x+","+y+"----Sorted possessed points: " + Arrays.toString(nearestCluster.toArray()));
            target = nearestCluster.get(nearestCluster.size() - 1);
            if(target.isPoint((int)x,(int)y)){
                return;
            }
        }

        int[][] path = botManager.getPath((int)x, (int)y,target, botCode);
        if(path != null && path.length < 2){
            System.out.println("Nasty bot did somethin wrong. Path from: " + (int)x +","+ (int)y + " to " +target);
            findRandomPath();
            return;
        }
        if(path == null){
            System.out.println("Nasty bot does somthing random, because no path was found.");
            findRandomPath();
            return;
        }
        setPath(path);
    }

    private Point getClosestTarget(List<Point> points, Comparator<Point> comparator){
        int targetIndex = 0;
        Collections.sort(points, comparator);
        if(points.get(targetIndex).isPoint((int)x,(int)y)){
            targetIndex++;
        }
        return points.get(1);
    }

}
