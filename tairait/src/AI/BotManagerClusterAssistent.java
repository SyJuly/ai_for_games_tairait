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

    public BotManagerClusterAssistent(BoardManager boardManager){
        this.boardManager = boardManager;
        clusterer = new Clusterer(boardManager);
    }

    @Override
    public void run() {
        while(true){
            enemyPossessedPointClusters = clusterer.cluster(boardManager.getEnemyPossessedPoints());
            nonPossessedPointClusters = clusterer.cluster(boardManager.getNonPossessedPoints());
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

    public List<List<Point>> getTeamPointClusters(){
        return teamPointClusters;
    }

    public List<List<Point>> getNonPossessedPointClusters(){
        return nonPossessedPointClusters;
    }

    public List<List<Point>> getEnemyPossessedPointClusters() {
        return enemyPossessedPointClusters;
    }
}
