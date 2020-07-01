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

    private long[] lastUpdated = new long[]{System.currentTimeMillis(), System.currentTimeMillis(),System.currentTimeMillis()};

    public void updateBotsTargets(){
        if(isRandom){
            for(Bot bot : bots){
                if(bot.arrivedAtTarget()){
                    bot.findRandomPath();
                }
            }
            return;
        }

        bots[0].updateTarget(boardManager.getEnemyPossessedPoints());
        bots[1].updateTarget(boardManager.getNonPossessedPoints());
        bots[2].updateTarget(boardManager.getEnemyPossessedPoints());

        for(Bot bot: bots){
            bot.updatePath();
        }


        //System.out.println("Update every " + (System.currentTimeMillis() -lastUpdated) * 1.0/1000.0 + " seconds.");
        //lastUpdated = System.currentTimeMillis();

    }
}
