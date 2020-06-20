import pathFinding.AStar;

public class MoveDirector {
    private final int NUM_OF_BOTS = 3;

    private BoardManager boardManager;
    private AStar pathFinder;
    private int ownTeam = -1;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public MoveDirector(BoardManager boardManager){
        this.boardManager = boardManager;

        pathFinder = new AStar(boardManager.getBoard());
        bots[0] = new Bot(1.1f, 0);
        bots[1] = new Bot(1f, 1);
        bots[2] = new Bot(0.67f, 2);


    }

    public void directBots(){
        for(int i = 0; i < 3; i++){
            int gridX = (int)bots[i].getX();
            int gridY = (int)bots[i].getY();

            int targetX = 20;
            int targetY = 20;
            int[][] board = boardManager.getBoard();
            while(board[targetX][targetY] < 0){
                targetX++;
                targetY++;
            }
            int[][] path = pathFinder.AStarSearch(gridX,gridY,targetX,targetY);
            bots[i].setPath(path);
        }
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
