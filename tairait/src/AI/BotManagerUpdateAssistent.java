package AI;

import Board.BoardManager;
import Board.Point;
import PathFinding.AStar;

public class BotManagerUpdateAssistent implements Runnable {

    private BoardManager boardManager;
    private boolean isRandom;
    private Bot[] bots;

    public BotManagerUpdateAssistent(BoardManager boardManager, boolean isRandom, Bot[] bots){
        this.boardManager = boardManager;
        this.isRandom = isRandom;
        this.bots = bots;

    }

    @Override
    public void run() {
        while(true){
            updateBotsTargets();
        }
    }

    private long lastUpdated = System.currentTimeMillis();

    public void updateBotsTargets(){
        if(isRandom){
            for(Bot bot : bots){
                if(bot.arrivedAtTarget()){
                    bot.findRandomPath();
                }
            }
            return;
        }


        if(bots[0].arrivedAtTarget()){
            bots[0].findNextPath(boardManager.getPossessedPoints());
            System.out.println("Changed path of Bot 0");
        }
        if(bots[1].arrivedAtTarget()){
            bots[1].findNextPath(boardManager.getNonPossessedPoints());
            System.out.println("Changed path of Bot 1");
        }
        if(bots[2].arrivedAtTarget()){
            bots[2].findNextPath(boardManager.getPossessedPoints());
            System.out.println("Changed path of Bot 2");
        }

        //System.out.println("Update every " + (System.currentTimeMillis() -lastUpdated) * 1.0/1000.0 + " seconds.");
        lastUpdated = System.currentTimeMillis();

    }
}
