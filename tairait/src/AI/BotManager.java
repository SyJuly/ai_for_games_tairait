package AI;

import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;
import lenz.htw.tiarait.net.NetworkClient;

import java.util.List;

public class BotManager {
    private final int NUM_OF_BOTS = 3;

    private AStar pathFinder;
    private BoardManager boardManager;
    private BotManagerClusterAssistent botManagerClusterAssistent;
    private BotManagerUpdateAssistent botManagerUpdateAssistent;
    private int ownTeam = -1;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public BotManager(BoardManager boardManager, boolean isRandom, NetworkClient nc){
        this.boardManager = boardManager;
        botManagerClusterAssistent = new BotManagerClusterAssistent(boardManager);
        pathFinder = new AStar(boardManager.getBoard());
        bots[1] = new BotQuick(this, botManagerClusterAssistent, null);
        bots[0] = new BotNasty(this, botManagerClusterAssistent);
        bots[2] = new BotBold(this, botManagerClusterAssistent,nc, null);

        botManagerUpdateAssistent = new BotManagerUpdateAssistent(boardManager, isRandom, bots);

        new Thread(botManagerClusterAssistent).start();
        new Thread(botManagerUpdateAssistent).start();
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
        for(Bot bot: bots){
            bot.ownTeamCode = ownTeam + 1;
        }
        pathFinder.setOwnTeamCode(ownTeam + 1);
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

    public boolean isBoardPointValid(int x, int y){
        return boardManager.getBoard()[x][y].statusCode >= 0 && BoardManager.isInInnerRing(x, y);
    }

    public List<Point> getOwnedPoints() {
        return boardManager.getOwnedPoints();
    }
}
