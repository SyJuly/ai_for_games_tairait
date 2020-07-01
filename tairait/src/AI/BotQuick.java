package AI;

import Board.Point;

import java.util.List;

public class BotQuick extends Bot {

    public BotQuick(BotManager botManager, BotManagerClusterAssistent assistent) {
        super(botManager, assistent,1.1f, 1);
    }

    @Override
    public void updateTarget(List<Point> allFreePoints) {


        if(!arrivedAtTarget()) {
            return;
        }

        Point target;
        if (allFreePoints.size() < 1) {
            return;
        }

        List<List<Point>> clusters = clusterAssistent.getNonPossessedPointClusters();
        if (clusters.size() < 1) {
            return;
        }
        List<Point> biggestCluster = getBiggestCluster(clusters);
        target = getTargetWithMaxDistance(biggestCluster);

        if (target == null || target.isPoint((int) x, (int) y)) {
            return;
        }currentTarget = target;

    }




    //Search free spaces away from other bots

}
