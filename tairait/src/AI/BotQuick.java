package AI;

import Board.Point;

import java.util.List;

public class BotQuick extends Bot {

    public BotQuick(BotManager botManager, BotManagerClusterAssistent assistent) {
        super(botManager, assistent,1.1f, 1);
    }

    @Override
    public void findNextPath(List<Point> allFreePoints) {
        if(arrivedAtTarget()) {
            if (allFreePoints.size() < 1) {
                findRandomPath();
                return;
            }

            List<List<Point>> clusters = clusterAssistent.getNonPossessedPointClusters();
            if (clusters.size() < 1) {
                findRandomPath();
                return;
            }
            List<Point> biggestCluster = getBiggestCluster(clusters);
            currentTarget = getTargetWithMaxDistance(biggestCluster);
        }

        if(currentTarget == null){
            return;
        }

        int[][] path = botManager.getPath((int)x, (int)y,currentTarget, botCode);
        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }




    //Search free spaces away from other bots

}
