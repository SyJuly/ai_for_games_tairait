package AI;

import Board.BoardManager;
import Board.Point;
import PathFinding.AStar;

public class BotManager {
    private final int NUM_OF_BOTS = 3;

    private BoardManager boardManager;
    private BotManagerClusterAssistent botManagerAssistent;
    private AStar pathFinder;
    private int ownTeam = -1;
    private boolean isRandom;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public BotManager(BoardManager boardManager, boolean isRandom){
        this.boardManager = boardManager;
        this.isRandom = isRandom;
        botManagerAssistent = new BotManagerClusterAssistent(boardManager);
        pathFinder = new AStar(boardManager.getBoard());

        bots[1] = new BotQuick(this, botManagerAssistent);
        bots[0] = new BotNasty(this, botManagerAssistent);
        bots[2] = new BotBold(this, botManagerAssistent);

        new Thread(botManagerAssistent).start();
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

    }


    public int[][] getPath(int startX, int startY, Point target, int botCode){
        boolean avoidCenter = botCode == 1 ? true: false;
        return pathFinder.AStarSearch(startX, startY,target.x,target.y, boardManager.getBoard(), avoidCenter, botCode);
    }

    public int[][] getPath(int startX, int startY, int targetX, int targetY, int botCode){
        boolean avoidCenter = botCode == 1 ? true: false;
        return pathFinder.AStarSearch(startX, startY,targetX,targetY, boardManager.getBoard(), avoidCenter, botCode);
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

    public AStar getPathFinder(){
        return pathFinder;
    }

    public boolean isBoardPointValid(int x, int y){
        return boardManager.getBoard()[x][y].statusCode >= 0 && BoardManager.isInInnerRing(x, y);
    }
}
