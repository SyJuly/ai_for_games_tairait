package AI;

import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.ArrayList;
import java.util.List;

public class BotManager {
    private final int NUM_OF_BOTS = 3;

    private BoardManager boardManager;
    private AStar pathFinder;
    private Clusterer clusterer;
    private int ownTeam = -1;
    private boolean isRandom;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public BotManager(BoardManager boardManager, boolean isRandom){
        this.boardManager = boardManager;
        this.isRandom = isRandom;
        pathFinder = new AStar(boardManager.getBoard());
        clusterer = new Clusterer(boardManager);
        bots[1] = new BotQuick(this);
        bots[0] = new BotNasty(this);
        bots[2] = new BotBold(this);


    }

    private long lastUpdated = System.currentTimeMillis();

    public void updateBotsTargets(){
        if(isRandom){
            for(Bot bot : bots){
                if(bot.arrivedAtTarget()){
                    bot.findRandomPath();
                }
            }
            return;
        }

        bots[0].findNextPath(boardManager.getPossessedPoints());
        bots[1].findNextPath(boardManager.getNonPossessedPoints());
        bots[2].findNextPath(boardManager.getPossessedPoints());

        System.out.println("Update every " + (System.currentTimeMillis() -lastUpdated) * 1.0/1000.0 + " seconds.");
        lastUpdated = System.currentTimeMillis();
        /*boolean graphHasBeenUpdated = false;
        if (bots[0].arrivedAtTarget()) {
            if (!graphHasBeenUpdated) {
                updateGraph();
                graphHasBeenUpdated = true;
            }
            bots[0].findNextPath(getPossedPoints());
        }
        if (bots[1].arrivedAtTarget()) {
            if (!graphHasBeenUpdated) {
                updateGraph();
                graphHasBeenUpdated = true;
            }
            bots[1].findNextPath(getNonPossedPoints());
        }
        if (bots[2].arrivedAtTarget()) {
            if (!graphHasBeenUpdated) {
                updateGraph();
                graphHasBeenUpdated = true;
            }
            bots[2].findNextPath(getBestTeamPoints());
        }*/

    }


    public int[][] getPath(int startX, int startY, Point target, int botCode){
        boolean avoidCenter = botCode == 1 ? true: false;
        return pathFinder.AStarSearch(startX, startY,target.x,target.y, boardManager.getBoard(), avoidCenter, botCode);
    }

    public int[][] getPath(int startX, int startY, int targetX, int targetY, int botCode){
        boolean avoidCenter = botCode == 1 ? true: false;
        return pathFinder.AStarSearch(startX, startY,targetX,targetY, boardManager.getBoard(), avoidCenter, botCode);
    }

    public void printCluster(){
        clusterer.printCluster(clusterer.cluster(boardManager.getTeams()[ownTeam].getPoints()));
    }

    public void setTeam(int ownTeam){
        this.ownTeam = ownTeam;
        this.pathFinder.setOwnTeamCode(ownTeam + 1);
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

    public boolean isUpdateRequired(){
        for(Bot bot : bots){
            if(bot.arrivedAtTarget()){
                return true;
            }
        }
        return false;
    }

    public Clusterer getClusterer() {
        return clusterer;
    }

    public AStar getPathFinder(){
        return pathFinder;
    }

    public boolean isBoardPointValid(int x, int y){
        return boardManager.getBoard()[x][y].statusCode >= 0 && BoardManager.isInInnerRing(x, y);
    }
}
