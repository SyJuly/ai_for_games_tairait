import lenz.htw.tiarait.net.NetworkClient;

public class BoardManager {

    public final int NUM_OF_PLAYERS = 4;
    public final int WORLD_SIZE = 32;
    /*
        Codes
        -1 = obstacle
         0 = empty
         1 = team 0
         2 = team 1
         3 = team 2
         4 = team 3
     */
    private int[][] board = new int[WORLD_SIZE][WORLD_SIZE];
    private Team[] teams = new Team[NUM_OF_PLAYERS];

    public BoardManager(){
        for(int i = 0; i < NUM_OF_PLAYERS; i++){
            teams[i] = new Team(i);
        }
    }

    public void setObstacles(NetworkClient client){
        int counter = 0;
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[x].length; y++){
                if(client.isWall(x,y)){
                    board[x][y] = -1;
                    counter++;
                    if((x >=13 && x <=20) && (y>=8 && y<=20)){
                        System.out.println("Obstacle: " + x + "|" + y);
                    }
                }

            }
        }
        System.out.println("Found " + counter + " obstacles.");
    }

    public void updateBoard(int x, int y, int code) {
        int prevCode = board[y][x];
        board[y][x] = code;
        if (code > 0) {
            teams[code - 1].addPoint(x, y);
        }
        if (prevCode > 0) {
            teams[prevCode - 1].removePoint(x, y);
        }
    }

    public void printScore(){
        for(int i = 0; i < NUM_OF_PLAYERS; i++){
            System.out.print("Team " + teams[i].getTeamCode() + ": " + teams[i].getPoints() + " | ");
        }
        System.out.println();
    }

    public int[][] getBoard() {
        return board;
    }
}
