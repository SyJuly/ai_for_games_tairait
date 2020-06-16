public class MoveDirector {
    private final int NUM_OF_BOTS = 3;

    private BoardManager boardManager;
    private int ownTeam = -1;
    private Bot[] bots = new Bot[NUM_OF_BOTS];

    public MoveDirector(BoardManager boardManager){
        this.boardManager = boardManager;

        bots[0] = new Bot(1.1f, 0);
        bots[1] = new Bot(1f, 1);
        bots[2] = new Bot(0.67f, 2);
    }

    public void setTeam(int ownTeam){
        this.ownTeam = ownTeam;
    }

    public int getTeam(){
        return ownTeam;
    }

    public int[] getMoveDirection(int botCode){
        return bots[botCode].getCurrentDirection();
    }
}
