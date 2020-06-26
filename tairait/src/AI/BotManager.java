package AI;

import Board.BoardManager;
import Board.Point;
import PathFinding.AStar;

public class BotManager {
    private final int NUM_OF_BOTS = 3;

    private BoardManager boardManager;
    private AStar pathFinder;
    private Clusterer clusterer;
    private int ownTeam = -1;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public BotManager(BoardManager boardManager){
        this.boardManager = boardManager;

        pathFinder = new AStar(boardManager.getBoard());
        clusterer = new Clusterer(boardManager);
        bots[0] = new BotQuick();
        bots[1] = new BotNasty();
        bots[2] = new BotBold();


    }

    public void directBots(){
        for(int i = 0; i < 3; i++){
            int gridX = (int)bots[i].getX();
            int gridY = (int)bots[i].getY();

            int targetX = 16;
            int targetY = 4;
            Point[][] board = boardManager.getBoard();
            while(board[targetX][targetY].statusCode < 0){
                targetX++;
                targetY++;
            }
            int[][] path = pathFinder.AStarSearch(gridX,gridY,targetX,targetY);
            bots[i].setPath(path);
        }
    }

    public void printCluster(){
        clusterer.printCluster(clusterer.cluster(boardManager.getTeams()[ownTeam].getPoints()));
    }

    public void setTeam(int ownTeam){
        this.ownTeam = ownTeam;
    }

    public int getTeam(){
        return ownTeam;
    }

    public float[] getMoveDirection(int botCode){
        return bots[botCode].getCurrentDirection();
    }

    public void updateBot(int botNr, float x, float y) {
        bots[botNr].updatePosition(x,y);
    }
}
