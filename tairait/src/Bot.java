public class Bot {
    private final float[] WAIT_DIRECTION = new float[]{0,0};
    private final float DISTANCE_THRESHOLD = 0.1f;

    public float speed;
    public int botCode;
    private float x;
    private float y;

    private double lastDistance = Double.POSITIVE_INFINITY;

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

        double distance = Math.hypot(x - currentTarget[0], y - currentTarget[1]);





        if(distance < DISTANCE_THRESHOLD) {
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
        }
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
        currentDirection = new float[]{currentTarget[0] - x, currentTarget[0] - y};
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
