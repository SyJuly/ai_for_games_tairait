package Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {
    private int teamCode;
    private List<Point> points;

    public Team(int teamCode){
        this.teamCode = teamCode;
        points = new ArrayList<>();
    }

    public void addPoint(Point point){
        // x? y?
        points.add(point);
    }

    public int getTeamCode() {
        return teamCode;
    }

    public void removePoint(Point point) {
        // x? y?
        points.remove(point);
    }

    public List<Point> getPoints(){
        return points;
    }
}
