package Board;

public class Point {
    public int x;
    public int y;
    public int statusCode;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        statusCode = 0; // empty
    }

    public String toString(){
        return "P(" + x + "," + y + "|" + statusCode + ")";
    }
}
