import java.util.ArrayList;
import java.util.List;

public class Team {
    private int teamCode;
    private int points;

    public Team(int teamCode){
        this.teamCode = teamCode;
    }

    public void addPoint(int x, int y){
        // x? y?
        points++;
    }

    public int getTeamCode() {
        return teamCode;
    }

    public void removePoint(int x, int y) {
        // x? y?
        points--;
    }

    public int getPoints(){
        return points;
    }
}
