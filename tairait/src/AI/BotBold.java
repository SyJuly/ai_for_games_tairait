package AI;

import Board.Point;
import lenz.htw.tiarait.net.NetworkClient;

import java.util.Collections;
import java.util.List;

public class BotBold extends Bot {

    private NetworkClient networkClient;
    //overwrite best and avoid own

    public BotBold(BotManager botManager, BotManagerClusterAssistent assistent, NetworkClient networkClient, Point initialTarget) {
        super(botManager, assistent,0.67f, 2);
        this.networkClient = networkClient;
        currentTarget = initialTarget;
    }

    @Override
    public void updateTarget(List<Point> allEnemiesPoints) {


        if(!arrivedAtTarget()) {
            return;
        }

        Point target;
        if (allEnemiesPoints.size() < 1) {
            return;
        }
        List<List<Point>> clusters = clusterAssistent.getTeamPointClusters();

        if (clusters.size() < 1) {
            target = getClosestTargetNotSelf(allEnemiesPoints);
        } else {
            List<Point> biggestCluster = getBiggestCluster(clusters);
            //System.out.println("Position: "+ x+","+y+"----Sorted possessed points: " + Arrays.toString(nearestCluster.toArray()));
            target = getTargetWithMaxDistance(biggestCluster);

        }
        if (target == null || target.isPoint((int) x, (int) y)) {
            return;
        }
        currentTarget = target;
    }


    public void findNextPath(List<Point> enemiesPoints) {
        int[][] path;


        if(arrivedAtTarget()) {
            Point target;
            List<List<Point>> clusters = clusterAssistent.getTeamPointClusters();

            if (clusters.size() < 1) {
                target = getClosestTargetNotSelf(enemiesPoints);
                if (enemiesPoints.size() < 1) {
                    return;
                }
            } else {
                List<Point> biggestCluster = getBiggestCluster(clusters);
                //System.out.println("Position: "+ x+","+y+"----Sorted possessed points: " + Arrays.toString(nearestCluster.toArray()));
                target = getTargetWithMaxDistance(biggestCluster);

            }
            if (target.isPoint((int) x, (int) y)) {
                return;
            }
            currentTarget = target;
        }

        path = botManager.getPath((int)x, (int)y,currentTarget.x,currentTarget.y, botCode);

        /*float[][] botPositions = new float[4][2];
        int nearestIndex = -1;
        float nearestDistance = Float.POSITIVE_INFINITY;
        for(int i = 0; i < 4; i++){
            botPositions[i] = new float[]{networkClient.getX(i,2), networkClient.getY(i,2)};
            if((int)botPositions[i][0] == (int)x && (int)botPositions[i][1] == (int)y){
                continue;
            }
            float distance = (botPositions[i][1] - y) * (botPositions[i][1] - y) + (botPositions[i][0] - x) * (botPositions[i][0] - x);
            float distanceToOwnedPoints = getDistanceToOwnedPoints(botPositions[i][0], botPositions[i][1]);
            if(distance < nearestDistance && distanceToOwnedPoints > 2){
                nearestDistance = distance;
                nearestIndex = i;
            }
        }*

        if(nearestIndex == -1){
            List<List<Point>> clusters = clusterAssistent.getEnemyPossessedPointClusters();
            if(clusters.size() < 1){
                findRandomPath();
                return;
            }
            List<Point> nearestCluster = getNearestCluster(clusters);
            Point target = getTargetWithMaxDistance(nearestCluster);
            path = botManager.getPath((int)x, (int)y,target.x,target.y, botCode);
        } else {
            path = botManager.getPath((int)x, (int)y,(int)botPositions[nearestIndex][0],(int)botPositions[nearestIndex][1], botCode);
        }*/


        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }

    private float getDistanceToOwnedPoints(float x, float y) {
        List<Point> points = botManager.getOwnedPoints();
        if(points.size() <= 0){
            return 0;
        }
        float minDistance = Float.POSITIVE_INFINITY;
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            float distance = (point.y - y) * (point.y - y) + (point.x - x) * (point.x - x);
            if((distance < minDistance && !point.isPoint((int)x, (int)y))){
                minDistance = distance;
            }
        }
        return minDistance;
    }
}
