public class MoveDirector {
    private BoardManager boardManager;
    private int ownTeam = -1;

    public  MoveDirector(BoardManager boardManager){
        this.boardManager = boardManager;
    }

    public void setTeam(int ownTeam){
        this.ownTeam = ownTeam;
    }

    public int getTeam(){
        return ownTeam;
    }
}
