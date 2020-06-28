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
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public BotManager(BoardManager boardManager){
        this.boardManager = boardManager;

        pathFinder = new AStar(boardManager.getBoard());
        clusterer = new Clusterer(boardManager);
        bots[0] = new BotQuick();
        bots[1] = new BotNasty();
        bots[2] = new BotBold();


    }

    public void updateBotsTargets(){
        updateGraph();
        bots[1].findNextPath(clusterer, pathFinder, getNonPossedPoints());
        bots[0].moveForward();
        bots[2].moveForward();
        //bots[1].findRandomPath(boardManager.getBoard(), pathFinder);
        //bots[2].findRandomPath(boardManager.getBoard(), pathFinder);
        //bots[1].findNextPath(clusterer, pathFinder, getPossedPoints());
        //bots[2].findNextPath(clusterer, pathFinder, getBestTeamPoints());
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
        pathFinder.updateOwnFieldAvoidence(boardManager.getBoard(), ownTeam + 1);
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
