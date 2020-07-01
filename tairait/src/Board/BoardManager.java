package Board;

import lenz.htw.tiarait.net.NetworkClient;

import java.util.ArrayList;
import java.util.List;

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
    private List<Point> enemyPossessedPoints = new ArrayList<>();
    private List<Point> nonPossessedPoints = new ArrayList<>();
    private Team[] teams = new Team[NUM_OF_PLAYERS];
    private int ownTeam;

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
        Point point = board[x][y];
        int prevCode = point.statusCode;
        point.statusCode = code;
        if(code == prevCode){
            return;
        }
        if (code > 0) {
            teams[code - 1].addPoint(point);
            if(prevCode == 0){
                if(code != (ownTeam + 1)){
                    enemyPossessedPoints.add(point);
                }
                nonPossessedPoints.remove(point);
            }
        }
        if (prevCode > 0) {
            teams[prevCode - 1].removePoint(point);

        }
        if(code == 0){
            enemyPossessedPoints.remove(point);
            nonPossessedPoints.add(point);
        }
    }

    public static boolean isInInnerRing(int x, int y){
        return (Math.sqrt(Math.pow(x-15, 2) + Math.pow(y-15,2)) < 14);
    }

    public List<Point> getOwnedPoints(){
        return teams[ownTeam].getPoints();
    }

    public void setOwnTeam(int ownTeam){
        this.ownTeam = ownTeam;
    }

    public void printScore(){
        for(int i = 0; i < NUM_OF_PLAYERS; i++){
            System.out.print("Team " + teams[i].getTeamCode() + ": " + teams[i].getPoints().size() + " | ");
        }
        System.out.println();
    }

    public Point[][] getBoard() {
        return board;
    }

    public Team[] getTeams() {
        return teams;
    }

    public List<Point> getEnemyPossessedPoints(){
        return enemyPossessedPoints;
    }
    public List<Point> getNonPossessedPoints(){
        return nonPossessedPoints;
    }


    public void printBoard(boolean print) {
        if(!print){
            return;
        }
        System.out.print("\n");
        for(int y = board.length -1; y >= 0; y--){
            for(int x = 0; x < board[y].length; x++){
                if(board[x][y].statusCode == -1){
                    System.out.print(BLACK + "X");
                }
                if(board[x][y].statusCode == 0){
                    System.out.print(WHITE + "X");
                }
                if(board[x][y].statusCode == 1){
                    System.out.print(RED + "X");
                }
                if(board[x][y].statusCode == 2){
                    System.out.print(BLUE + "X");
                }
                if(board[x][y].statusCode == 3){
                    System.out.print(YELLOW + "X");
                }
                if(board[x][y].statusCode == 4){
                    System.out.print(GREEN + "X");
                }
                System.out.print("\033[0m");
            }
            System.out.print(BLACK+"\n");
            System.out.print("\033[0m");

        }
        System.out.println();
        printScore();
        System.out.println();
    }
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String WHITE = "\033[0;35m";   // WHITE

    public List<List<Point>> getEnemyPossessedPointsDifferentiated() {
        List<List<Point>> pointsOfEnemies = new ArrayList<>();
        for(int i = 0; i < NUM_OF_PLAYERS; i++){
            if(i == ownTeam){
                continue;
            }
            pointsOfEnemies.add(teams[i].getPoints());
        }
        return pointsOfEnemies;
    }

    public Point getXAxisPoint(int i) {
        Point point = null;
        int index = i < 0 ? WORLD_SIZE - 1 : 0;
        while(point == null){
            index += i;
            if(board[index][WORLD_SIZE/2].statusCode >= 0 && isInInnerRing(index, WORLD_SIZE/2)){
                point = board[index][WORLD_SIZE/2];
            }
        }
        return point;
    }
}
