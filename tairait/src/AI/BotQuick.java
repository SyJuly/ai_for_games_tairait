package AI;

import Board.BoardManager;
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



        /*Primary Strategy*/
        Point target = null;

        if(botsAreFarAway()){
            target = getClosestTargetNotSelf(clusterAssistent.getNonAndEnemyPossessedPoints());
        } else {
            target = getPointFurthestFromOtherBots();
        }

        /*Alternative Strategy*/
        if(target == null){
            target = getFurthestEmptyPoint();
        }


        if (target == null || target.isPoint((int) x, (int) y)) {
            return;
        }

        currentTarget = target;

    }

    private boolean botsAreFarAway() {
        for(int t = 0; t < 4; t++){
            for(int b = 0; b < 3; b++){
                float teamBotX = networkClient.getX(t,b);
                float teamBotY = networkClient.getY(t,b);
                if((y - teamBotY) * (y - teamBotY) + (x - teamBotX) * (x - teamBotX) < 16){
                    return false;
                }
            }

        }
        return true;
    }

    private Point getFurthestEmptyPoint(){
        List<List<Point>> clusters = clusterAssistent.getNonPossessedPointClusters();
        if (clusters.size() < 1) {
            return null;
        }
        List<Point> biggestCluster = getBiggestCluster(clusters);
        return getTargetWithMaxDistance(biggestCluster);
    }

    private Point getPointFurthestFromOtherBots() {
        Point target = null;
        float highestDistance = Float.NEGATIVE_INFINITY;
        List<Point> points = clusterAssistent.getNonAndEnemyPossessedPoints();
        float[] distances = new float[points.size()];
        for(int i = 0; i < distances.length; i++){
            Point p = points.get(i);
            if(!BoardManager.isInInnerRing(p.x,p.y)){
                continue;
            }
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
        return target;
    }


    //Search free spaces away from other bots

}
