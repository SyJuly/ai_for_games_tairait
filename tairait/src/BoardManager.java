public class BoardManager {

    public int[][] board = new int[32][32];

    public void setObstacle(int x, int y){
        board[x][y] = -1;
    }
}
