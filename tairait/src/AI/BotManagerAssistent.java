package AI;

import Board.BoardManager;
import Board.Point;

import java.util.List;

public class BotManagerAssistent implements Runnable {

    private BoardManager boardManager;
    private Clusterer clusterer;
    private List<List<Point>> possessedPointClusters;
    private List<List<Point>> nonPossessedPointClusters;

    public BotManagerAssistent(BoardManager boardManager){
        this.boardManager = boardManager;
        clusterer = new Clusterer(boardManager);
    }

    @Override
    public void run() {
        while(true){
            possessedPointClusters = clusterer.cluster(boardManager.getPossessedPoints());
            nonPossessedPointClusters = clusterer.cluster(boardManager.getNonPossessedPoints());
        }
    }

    public List<List<Point>> getPossessedPointClusters(){
        return possessedPointClusters;
    }

    public List<List<Point>> getNonPossessedPointClusters(){
        return nonPossessedPointClusters;
    }
}
