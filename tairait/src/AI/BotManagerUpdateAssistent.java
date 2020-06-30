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

        bots[0].findNextPath(boardManager.getEnemyPossessedPoints());
        bots[1].findNextPath(boardManager.getNonPossessedPoints());
        bots[2].findNextPath(boardManager.getEnemyPossessedPoints());

        /*
        if(bots[0].arrivedAtTarget() || (System.currentTimeMillis() - lastUpdated[0]) > 3){
            bots[0].findNextPath(boardManager.getEnemyPossessedPoints());
            System.out.println("Changed path of Bot 0");
            lastUpdated[0] = System.currentTimeMillis();
        }
        if(bots[1].arrivedAtTarget()|| (System.currentTimeMillis() - lastUpdated[1]) > 3){
            bots[1].findNextPath(boardManager.getNonPossessedPoints());
            System.out.println("Changed path of Bot 1");
            lastUpdated[1] = System.currentTimeMillis();
        }
        if(bots[2].arrivedAtTarget() || (System.currentTimeMillis() - lastUpdated[2]) > 3){
            bots[2].findNextPath(boardManager.getEnemyPossessedPoints());
            System.out.println("Changed path of Bot 2");
            lastUpdated[2] = System.currentTimeMillis();
        }*/

        //System.out.println("Update every " + (System.currentTimeMillis() -lastUpdated) * 1.0/1000.0 + " seconds.");
        //lastUpdated = System.currentTimeMillis();

    }
}
