package AI;

import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.List;
import java.util.Random;

public abstract class Bot {
    private final float[] WAIT_DIRECTION = new float[]{0,0};

    protected BotManager botManager;
    public float speed;
    public int botCode;
    protected float x;
    protected float y;

    private float lastX;
    private float lastY;

    private int[][] path;
    private int pathIndex = -1;
    public float[] currentDirection = WAIT_DIRECTION;


    public Bot(BotManager botManager, float speed, int botCode){
        this.botManager = botManager;
        this.speed = speed;
        this.botCode = botCode;
    }

    public void updatePosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCurrentDirection();
    }

    private void updateCurrentDirection() {
        if(path == null){
            currentDirection = WAIT_DIRECTION;
            return;
        }

        int[] currentTarget = path[pathIndex];
        if(hasSteppedOnTarget(currentTarget)){
            //System.out.println("AI.Bot:" + botCode+" has passed target was true: " + pathIndex + "| direction:" + currentDirection[0]+","+currentDirection[1]);
            pathIndex++;
            if(pathIndex > path.length - 1){
                path = null;
                currentDirection = WAIT_DIRECTION;
                System.out.println("VIIIIIIIIIIIIICTTTOOORYYYY BOT " + botCode + " REACHED TARGET: " + x + "|" + y);
                return;
            }
            this.lastX = x;
            this.lastY = y;
        }

        setDirection();
    }

    private boolean hasSteppedOnTarget(int[] currentTarget){
        if((currentTarget[0] == (int)x && currentTarget[1] == (int)y)){
            return true;
        }
        if((currentDirection[0] >= 0 && currentTarget[0] >= lastX && currentTarget[0] <= x)
        || (currentDirection[0] < 0 && currentTarget[0] <= lastX && currentTarget[0] >= x)){
            float u = (currentTarget[0] - x)/currentDirection[0];
            int yi =(int) (y + u * currentDirection[1]);
            return currentTarget[1] == yi;
        }
        return false;
    }

    private void setDirection() {
        int[] currentTarget = path[pathIndex];
        this.lastX = x;
        this.lastY = y;
        float directionX = (currentTarget[0] +0.5f) - x;
        float directionY = (currentTarget[1] +0.5f) - y;
        currentDirection = new float[]{directionX, directionY};
    }

    public void setPath(int[][] path){
        this.path = path;
        this.pathIndex = 1;
        System.out.println("Setting path for bot: " + botCode + "| Current position: " + x + "," + y);
        setDirection();
    }

    public void findRandomPath(){
        Random random = new Random();
        boolean isValid = false;
        int randomX = 0;
        int randomY = 0;
        while(!isValid){
            randomX = random.nextInt(32);
            randomY = random.nextInt(32);
            if(botManager.isBoardPointValid(randomX, randomY)){
                isValid = true;
            }
        }

        int[][] path = botManager.getPathFinder().AStarSearch((int)x, (int)y, randomX, randomY, false);
        setPath(path);
    }

    public void moveForward(){
        currentDirection = new float[]{1,1};
    }

    public abstract void findNextPath(List<Point> points);

    public boolean arrivedAtTarget(){
        return path == null;
    }

    public float[] getCurrentDirection() {
        return currentDirection;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
