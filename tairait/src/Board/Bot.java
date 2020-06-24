package Board;

public class Bot {
    private final float[] WAIT_DIRECTION = new float[]{0,0};

    public float speed;
    public int botCode;
    private float x;
    private float y;

    private float lastX;
    private float lastY;

    private int[][] path;
    private int pathIndex = -1;
    public float[] currentDirection = WAIT_DIRECTION;


    public Bot(float speed, int botCode){
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
            //System.out.println("Board.Bot:" + botCode+" has passed target was true: " + pathIndex + "| direction:" + currentDirection[0]+","+currentDirection[1]);
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
        currentDirection = new float[]{currentTarget[0] - x, currentTarget[1] - y};
    }

    public void setPath(int[][] path){
        this.path = path;
        this.pathIndex = 1;
        System.out.println("Setting path for bot: " + botCode + "| Current position: " + x + "," + y);
        setDirection();
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
