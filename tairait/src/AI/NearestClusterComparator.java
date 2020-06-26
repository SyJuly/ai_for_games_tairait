package AI;

import Board.Point;

import java.util.Comparator;
import java.util.List;

public class NearestClusterComparator implements Comparator<List<Point>> {

    private float x;
    private float y;

    public NearestClusterComparator(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int compare(List<Point> l1, List<Point> l2) {
        float minDistance = Float.POSITIVE_INFINITY;
        boolean l1NearerThanl2 = true;
        for(int i = 0; i < l1.size(); i++){
            Point p = l1.get(i);
            float distance = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
            if(distance < minDistance){
                minDistance = distance;
            }
        }
        for(int i = 0; i < l2.size(); i++){
            Point p = l2.get(i);
            float distance = (p.y - y) * (p.y - y) + (p.x - x) * (p.x - x);
            if(distance < minDistance){
                l1NearerThanl2 = true;
            }
        }
        return l1NearerThanl2 ? 1 : -1;
    }
}
