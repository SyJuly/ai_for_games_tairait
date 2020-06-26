package AI;

import AI.Bot;
import Board.Point;
import Board.Team;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty() {
        super(1, 1);
    }

    @Override
    public void findNextPath(Point[][] board, Team[] teams) {

    }
}
