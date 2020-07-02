package AI;

import Board.Point;

import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty(BotManager botManager, BotManagerClusterAssistent assistent) {
        super(botManager, assistent,1, 0);
    }


    @Override
    public void updateTarget(List<Point> allEnemiesPoints) {

        if(!arrivedAtTarget() && currentTarget != null && currentTarget.statusCode != ownTeamCode) {
            return;
        }

        Point target;
        if (allEnemiesPoints.size() < 1) {
            return;
        }
        List<List<Point>> clusters = clusterAssistent.getEnemyPossessedPointClusters();

        if (clusters.size() < 1) {
            /*Secondary Strategy*/
            target = getClosestTargetNotSelf(allEnemiesPoints);
        } else {
            /*Primary Strategy*/
            List<Point> biggestCluster = getBiggestCluster(clusters);
            target = getClosestTargetNotSelf(biggestCluster);
        }
        if (target == null || target.isPoint((int) x, (int) y)) {
            return;
        }
        currentTarget = target;
    }

}
