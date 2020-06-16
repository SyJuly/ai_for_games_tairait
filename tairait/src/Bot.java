public class Bot {
    public float speed;
    public int botCode;
    public int[] currentDirection = new int[]{1,1};

    public Bot(float speed, int botCode){
        this.speed = speed;
        this.botCode = botCode;
    }

    public int[] getCurrentDirection() {
        return currentDirection;
    }
}
