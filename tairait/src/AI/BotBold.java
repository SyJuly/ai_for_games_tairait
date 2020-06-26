package AI;

import Board.Point;
import Board.Team;

public class BotBold extends Bot {

    //overwrite best and avoid own

    public BotBold() {
        super(0.67f, 2);
    }

    @Override
    public void findNextPath(Point[][] board, Team[] teams) {

    }

}
