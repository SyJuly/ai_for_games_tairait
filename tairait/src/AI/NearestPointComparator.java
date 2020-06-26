package AI;

import Board.Point;

import java.util.Comparator;

public class NearestPointComparator implements Comparator<Point> {

    private float x;
    private float y;

    public NearestPointComparator(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int compare(Point p1, Point p2) {
        float distanceP1 = (p1.y - y) * (p1.y - y) + (p1.x - x) * (p1.x - x);
        float distanceP2 = (p2.y - y) * (p2.y - y) + (p2.x - x) * (p2.x - x);
        return Float.compare(distanceP1, distanceP2);
    }
}
