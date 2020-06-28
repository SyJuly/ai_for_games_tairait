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

    public void updateBotsTargets(){
        if(isRandom){
            for(Bot bot : bots){
                if(bot.arrivedAtTarget()){
                    bot.findRandomPath();
                }
            }
            return;
        }


        boolean graphHasBeenUpdated = false;
        if (bots[0].arrivedAtTarget()) {
            if (!graphHasBeenUpdated) {
                updateGraph();
                graphHasBeenUpdated = true;
            }
            bots[0].findNextPath(getPossedPoints());
        }
        if (bots[1].arrivedAtTarget()) {
            bots[1].findRandomPath();
        }
        if (bots[2].arrivedAtTarget()) {
            bots[2].findRandomPath();
        }

        //bots[1].findNextPath(getPossedPoints());
        //bots[2].findNextPath(getBestTeamPoints());
    }

    private List<Point> getBestTeamPoints() {
        Team[] teams = boardManager.getTeams();
        List<Point> bestTeamPoints = teams[0].getPoints();
        for(int i = 1; i < teams.length; i++){
            if(bestTeamPoints.size() < teams[i].getPoints().size()){
                bestTeamPoints = teams[i].getPoints();
            }
        }
        if(bestTeamPoints.size() == 0){
            System.out.println("Something went wrong. Could not find no best team points.");
        }
        return bestTeamPoints;
    }

    private List<Point> getPossedPoints() {
        List<Point> possedPoints = new ArrayList<>();
        Team[] teams = boardManager.getTeams();
        for(Team team : teams){
            possedPoints.addAll(team.getPoints());
        }
        if(possedPoints.size() == 0){
            System.out.println("Something went wrong. Could not find no possed points.");
        }
        return possedPoints;
    }

    private List<Point> getNonPossedPoints() {
        List<Point> nonPossedPoints = new ArrayList<>();
        Point[][] board = boardManager.getBoard();
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                if(board[x][y].statusCode == 0 && BoardManager.isInInnerRing(x,y)){
                    nonPossedPoints.add(board[x][y]);
                }
            }
        }
        if(nonPossedPoints.size() == 0){
            System.out.println("Something went wrong. Could not find no non-possed points.");
        }
        return nonPossedPoints;
    }

    public void updateGraph(){
        pathFinder.updatePreferenceCosts(boardManager.getBoard(), ownTeam + 1);
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
