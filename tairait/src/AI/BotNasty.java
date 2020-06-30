package AI;

import Board.Point;

import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty(BotManager botManager, BotManagerClusterAssistent assistent) {
        super(botManager, assistent,1, 0);
    }


    @Override
    public void findNextPath(List<Point> allEnemiesPoints) {

        Point target;
        if(arrivedAtTarget() || currentTarget.isPoint((int)x, (int)y)) {
            if (allEnemiesPoints.size() < 1) {
                return;
            }
            List<List<Point>> clusters = clusterAssistent.getEnemyPossessedPointClusters();

            if (clusters.size() < 1) {
                target = getClosestTargetNotSelf(allEnemiesPoints);
            } else {
                List<Point> nearestCluster = getNearestCluster(clusters);
                //System.out.println("Position: "+ x+","+y+"----Sorted possessed points: " + Arrays.toString(nearestCluster.toArray()));
                target = getClosestTargetNotSelf(nearestCluster);

            }
            if (target.isPoint((int) x, (int) y)) {
                return;
            }
            currentTarget = target;
        }

        int[][] path = botManager.getPath((int)x, (int)y,currentTarget, botCode);
        if(path != null && path.length < 2){
            System.out.println("Nasty bot did somethin wrong. Path from: " + (int)x +","+ (int)y + " to " +currentTarget + "...pathIndex?" + pathIndex + "-pathlength: " + path.length);
            //findRandomPath();
            return;
        }
        if(path == null){
            System.out.println("Nasty bot does somthing random, because no path was found.");
            //findRandomPath();
            return;
        }
        setPath(path);
    }

}
