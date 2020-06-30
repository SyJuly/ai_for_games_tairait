package AI;

import Board.BoardManager;
import Board.Point;

import java.util.List;

public class BotManagerClusterAssistent implements Runnable {

    private BoardManager boardManager;
    private Clusterer clusterer;
    private List<List<Point>> enemyPossessedPointClusters;
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
        }
    }

    public List<List<Point>> getNonPossessedPointClusters(){
        return nonPossessedPointClusters;
    }

    public List<List<Point>> getEnemyPossessedPointClusters() {
        return enemyPossessedPointClusters;
    }
}
