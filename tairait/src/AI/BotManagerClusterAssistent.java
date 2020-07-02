package AI;

import Board.BoardManager;
import Board.Point;

import java.util.ArrayList;
import java.util.List;

public class BotManagerClusterAssistent implements Runnable {

    private BoardManager boardManager;
    private Clusterer clusterer;
    private List<List<Point>> enemyPossessedPointClusters;
    private List<List<Point>> teamPointClusters;
    private List<List<Point>> nonPossessedPointClusters;

    private boolean isStopped = false;

    public BotManagerClusterAssistent(BoardManager boardManager){
        this.boardManager = boardManager;
        clusterer = new Clusterer(boardManager);
        enemyPossessedPointClusters = new ArrayList<>();
        teamPointClusters = new ArrayList<>();
        nonPossessedPointClusters = new ArrayList<>();
    }

    @Override
    public void run() {
        while(!isStopped){
            enemyPossessedPointClusters = clusterer.cluster(copyPointList(boardManager.getEnemyPossessedPoints()));
            nonPossessedPointClusters = clusterer.cluster(copyPointList(boardManager.getNonPossessedPoints()));
            teamPointClusters = getEnemyPossessedPointClustersDifferentiated();
        }
    }

    private List<List<Point>> getEnemyPossessedPointClustersDifferentiated() {
        List<List<Point>> clusters = new ArrayList<>();
        List<List<Point>> enemyPoints = boardManager.getEnemyPossessedPointsDifferentiated();
        for(int i = 0; i < enemyPoints.size(); i++){
            clusters.addAll(clusterer.cluster(enemyPoints.get(i)));
        }
        return clusters;
    }

    private List<Point> copyPointList(List<Point> points){
        List<Point> copiedList = new ArrayList<>();
        copiedList.addAll(points);
        return copiedList;
    }

    public List<List<Point>> getTeamPointClusters(){
        return teamPointClusters;
    }

    public List<List<Point>> getNonPossessedPointClusters(){
        return nonPossessedPointClusters;
    }

    public List<List<Point>> getEnemyPossessedPointClusters() {
        return enemyPossessedPointClusters;
    }

    public List<Point> getNonAndEnemyPossessedPoints(){
        List<Point> points = new ArrayList<>();
        points.addAll(boardManager.getEnemyPossessedPoints());
        points.addAll(boardManager.getNonPossessedPoints());
        return points;
    }

    public void stop(){
        isStopped = true;
    }
}
