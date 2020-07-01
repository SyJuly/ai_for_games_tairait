package AI;

import Board.Point;
import lenz.htw.tiarait.net.NetworkClient;

import java.util.List;

public class BotQuick extends Bot {

    NetworkClient networkClient;
    public BotQuick(BotManager botManager, BotManagerClusterAssistent assistent, NetworkClient nc, Point initialTarget) {
        super(botManager, assistent,1.1f, 1);
        currentTarget = initialTarget;
        this.networkClient = nc;
    }

    @Override
    public void updateTarget(List<Point> allFreePoints) {


        if(!arrivedAtTarget() && currentTarget != null &&  currentTarget.statusCode != ownTeamCode) {
            return;
        }

        Point target = null;
        if (allFreePoints.size() < 1) {
            return;
        }

        float highestDistance = Float.NEGATIVE_INFINITY;
        List<Point> points = clusterAssistent.getNonAndEnemyPossessedPoints();
        float[] distances = new float[points.size()];
        for(int i = 0; i < distances.length; i++){
            Point p = points.get(i);
            float distanceSum = 0;
            float distanceToSelf = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
            if(distanceToSelf < 80){
                continue;
            }
            for(int t = 0; t < 4; t++){
                for(int b = 0; b < 3; b++){
                    float teamBotX = networkClient.getX(t,b);
                    float teamBotY = networkClient.getY(t,b);
                    distanceSum += (p.y - teamBotY) * (p.y - teamBotY) + (p.x - teamBotX) * (p.x - teamBotX);
                }

            }
            if(distanceSum > highestDistance){
                highestDistance = distanceSum;
                target = p;
            }
        }

        /*List<List<Point>> clusters = clusterAssistent.getNonPossessedPointClusters();
        if (clusters.size() < 1) {
            return;
        }
        List<Point> biggestCluster = getBiggestCluster(clusters);
        target = getTargetWithMaxDistance(biggestCluster);*/

        if (target == null || target.isPoint((int) x, (int) y)) {
            return;
        }

        currentTarget = target;

    }




    //Search free spaces away from other bots

}
