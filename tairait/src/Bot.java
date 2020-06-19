public class Bot {
    private final float[] WAIT_DIRECTION = new float[]{0,0};
    private final float DISTANCE_THRESHOLD = 0.2f;

    public float speed;
    public int botCode;
    private float x;
    private float y;

    //private double lastDistance = Double.POSITIVE_INFINITY;
    private int lastX;
    private int lastY;

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
        /*if(pathIndex == path.length - 1 && (int)x == path[path.length - 1][0] && (int)y == path[path.length - 1][1]){
            //reached final goal
            path = null;
            currentDirection = WAIT_DIRECTION;
            System.out.println("VIIIIIIIIIIIIICTTTOOORYYYY BOT " + botCode + " REACHED TARGET: " + x + "|" + y);
            return;
        }*/

        int[] currentTarget = path[pathIndex];
        if(hasPassedTarget(currentTarget)){
            System.out.println("has passed target was true: " + pathIndex);

            pathIndex++;
            if(pathIndex > path.length - 1){
                System.out.println("Something went wrong, passed target without passing target: P(" + x + "|" + y + "), PL("+ lastX + "|" + lastY + ") -----target was: " + path[pathIndex - 1][0] + "|" + path[pathIndex - 1][1]);
            }
            this.lastX = (int)x;
            this.lastY = (int)y;
        }
        setDirection();
        //double distance = Math.hypot(x - currentTarget[0], y - currentTarget[1]);



        /*if(distance < DISTANCE_THRESHOLD) {
            //System.out.println("Targeting next node: " + pathIndex);
            pathIndex++;
            lastDistance = distance;
            if(pathIndex >= path.length ){
                path = null;
                currentDirection = WAIT_DIRECTION;
                System.out.println("VIIIIIIIIIIIIICTTTOOORYYYY BOT " + botCode + " REACHED TARGET");
                return;
            }
            setDirection();
            return;
        }
        if(distance > lastDistance || Math.abs(lastDistance - distance) < 0.01){
            lastDistance = distance;
            int closestPathIndex = correctDirection();
            pathIndex = closestPathIndex;
            setDirection(); //drove by target, correcting
            return;
        }*/
    }


    private boolean hasPassedTarget(int[] currentTarget) {

        int dxc = currentTarget[0] - (int)x;
        float dyc = currentTarget[1] - (int)y;

        float dxl = lastX - (int)x;
        float dyl = lastY - (int)y;
        float cross = dxc * dyl - dyc * dxl;
        /*if(botCode == 0 && path != null) {
            System.out.print("Bot: " + botCode + "::: ");
            System.out.print("LastX: " + lastX + "| LastY: " + lastY + "--------- X: " + x + " | Y: " + y);
            System.out.print(" --------- Current Target: " + currentTarget[0] + "|" + currentTarget[1]);
            System.out.print(" --------- Current direction: " + currentDirection[0] + "|" + currentDirection[1]);
            System.out.println(" ////cross: " + cross);
        }*/

        if (cross != 0)
            return false;

        if (Math.abs(dxl) >= Math.abs(dyl))
            return dxl > 0 ?
                    (int)x <= currentTarget[0] && currentTarget[0] <= lastX :
                    lastX <= currentTarget[0] && currentTarget[0] <= (int)x;
        else
            return dyl > 0 ?
                    (int)y <= currentTarget[1] && currentTarget[1] <= lastY :
                    lastY <= currentTarget[1] && currentTarget[1] <= (int)y;

    }

    private boolean passedTarget(int[] currentTarget) {
        float dx = lastX - x;
        float dy = lastY - y;



    }

    private int correctDirection() {
        double minDistance = Double.POSITIVE_INFINITY;
        for(int i = 0; i < path.length; i++){
            double distance = Math.hypot(x - path[i][0], y - path[i][1]);
            if(distance < minDistance){
                minDistance = distance;
            } else {
                return i - 1;
            }
        }
        return pathIndex;
    }


    private void setDirection() {
        int[] currentTarget = path[pathIndex];
        this.lastX = (int)x;
        this.lastY = (int)y;
        currentDirection = new float[]{currentTarget[0] - x, currentTarget[1] - y};
    }

    public void setPath(int[][] path){
        this.path = path;
        this.pathIndex = 0;
        System.out.println("Setting path for bot: " + botCode);
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
