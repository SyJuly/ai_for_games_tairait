package AI;

import Board.Point;
import PathFinding.AStar;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Bot {
    private final float[] WAIT_DIRECTION = new float[]{0,0};
    private final float MAX_TARGET_DISTANCE_PER_SECOND = 6;

    protected BotManager botManager;
    protected BotManagerClusterAssistent clusterAssistent;
    public float speed;
    public int botCode;
    protected float maxTargetDistance;
    protected float x;
    protected float y;

    private float lastX;
    private float lastY;

    private int[][] path;
    private int pathIndex = -1;
    public float[] currentDirection = WAIT_DIRECTION;


    public Bot(BotManager botManager, BotManagerClusterAssistent clusterAssistent, float speed, int botCode){
        this.botManager = botManager;
        this.clusterAssistent = clusterAssistent;
        this.speed = speed;
        this.botCode = botCode;
        this.maxTargetDistance = speed * MAX_TARGET_DISTANCE_PER_SECOND;
    }

    public void updatePosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCurrentDirection();
    }

    private void updateCurrentDirection() {
        if(path == null || pathIndex < 0){
            currentDirection = WAIT_DIRECTION;
            return;
        }

        int[] currentTarget = path[pathIndex];
        if(hasSteppedOnTarget(currentTarget)){
            //System.out.println("AI.Bot:" + botCode+" has passed target was true: " + pathIndex + "| direction:" + currentDirection[0]+","+currentDirection[1]);
            pathIndex++;
            //botManager.get.releasePredictedOwnership(currentTarget[0], currentTarget[1], botCode);
            if(pathIndex > path.length - 1){
                path = null;
                currentDirection = WAIT_DIRECTION;
                if(botCode == 0){
                    //System.out.println("VIIIIIIIIIIIIICTTTOOORYYYY BOT " + botCode + " REACHED TARGET: " + x + "|" + y);
                }
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
        if(path != null && path.length < 2){
            System.out.println("Invalid path: " + Arrays.toString(path[0]) + " to " + Arrays.toString(path[path.length-1]));
        }
        this.pathIndex = 1;
        this.path = path;
        //System.out.println("Setting path for bot: " + botCode + "| Current position: " + x + "," + y);
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
            if(botManager.isBoardPointValid(randomX, randomY) && ((x != randomX) || (y != randomY))){
                isValid = true;
            }
        }

        int[][] path = botManager.getPath((int)x, (int)y, randomX, randomY, botCode);
        if(path == null || path.length < 2){
            findRandomPath();
            return;
        }
        setPath(path);
    }


    protected Point getTargetWithMaxDistance(List<Point> points){
        float validMaxDistance = Float.NEGATIVE_INFINITY;
        Point validMaxPoint = null;
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            float distance = (point.y - y) * (point.y - y) + (point.x - x) * (point.x - x);
            if(validMaxPoint == null || (distance > validMaxDistance && distance <= maxTargetDistance)){
                validMaxDistance = distance;
                validMaxPoint = point;
            }
        }
        return validMaxPoint;
    }

    protected Point getClosestTargetNotSelf(List<Point> points){
        float validMinDistance = Float.POSITIVE_INFINITY;
        Point validMinPoint = null;
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            float distance = (point.y - y) * (point.y - y) + (point.x - x) * (point.x - x);
            if(validMinPoint == null || (distance < validMinDistance && !point.isPoint((int)x, (int)y))){
                validMinDistance = distance;
                validMinPoint = point;
            }
        }
        return validMinPoint;
    }

    protected List<Point> getNearestCluster(List<List<Point>> clusters) {
        List<Point> nearestCluster = null;
        float minDistance = Float.POSITIVE_INFINITY;
        for (int i = 0; i < clusters.size(); i++) {
            List<Point> cluster = clusters.get(i);
            for (int j = 0; j < cluster.size(); j++) {
                Point p = cluster.get(j);
                float distance = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
                if (nearestCluster == null || distance < minDistance) {
                    minDistance = distance;
                    nearestCluster = cluster;
                }
            }
        }
        return nearestCluster;
    }

    protected List<Point> getBiggestCluster(List<List<Point>> clusters) {
        List<Point> biggestCluster = clusters.get(0);
        for(int i = 1; i < clusters.size(); i++){
            if(clusters.get(i).size() > biggestCluster.size()){
                biggestCluster = clusters.get(i);
            }
        }
        return biggestCluster;
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
