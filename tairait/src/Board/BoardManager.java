package Board;

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
    private Point[][] board = new Point[WORLD_SIZE][WORLD_SIZE];
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
                board[x][y] = new Point(x,y);
                if(client.isWall(x,y)){
                    board[x][y].statusCode = -1;
                    counter++;
                }

            }
        }
        System.out.println("Found " + counter + " obstacles.");
    }

    public void updateBoard(int x, int y, int code) {
        Point point = board[y][x];
        int prevCode = point.statusCode;
        point.statusCode = code;
        if (code > 0) {
            teams[code - 1].addPoint(point);
        }
        if (prevCode > 0) {
            teams[prevCode - 1].removePoint(point);
        }
    }

    public void printScore(){
        for(int i = 0; i < NUM_OF_PLAYERS; i++){
            System.out.print("Board.Team " + teams[i].getTeamCode() + ": " + teams[i].getPoints() + " | ");
        }
        System.out.println();
    }

    public Point[][] getBoard() {
        return board;
    }
}
