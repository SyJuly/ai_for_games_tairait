import lenz.htw.tiarait.net.NetworkClient;

public class BoardManager {

    /*
        Codes
        -1 = obstacle
         0 = empty
         1 = team 0
         2 = team 1
         3 = team 2
         4 = team 3
     */
    private int[][] board = new int[32][32];

    public void setObstacles(NetworkClient client){
        int counter = 0;
        for(int y = 0; y < board.length; y++){
            for(int x = 0; x < board[y].length; x++){
                if(client.isWall(x,y)){
                    board[y][x] = -1;
                    counter++;

                }
            }
        }
        System.out.println("Found " + counter + " obstacles.");
    }

    public void updateBoard(int x, int y, int code) {
        board[y][x] = code;
        System.out.println("Updated board: " + code);
    }
}
