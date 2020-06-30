package AI;

import Board.Point;

import java.util.Collections;
import java.util.List;

public class BotBold extends Bot {

    //overwrite best and avoid own

    public BotBold(BotManager botManager, BotManagerClusterAssistent assistent) {
        super(botManager, assistent,0.67f, 2);
    }


    @Override
    public void findNextPath(List<Point> enemiesPoints) {
        List<List<Point>> clusters = assistent.getPossessedPointClusters();
        if(clusters.size() < 1){
            findRandomPath();
            return;
        }
        List<Point> biggestCluster = getBiggestCluster(clusters);
        Collections.sort(biggestCluster, new NearestPointComparator(x,y));
        Point target = getTargetWithMaxDistance(biggestCluster);
        //System.out.println("Bold one targets: " + target + " from " + clusters.size() + " clusters.");

        int[][] path = botManager.getPath((int)x, (int)y,target, botCode);
        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }
}
