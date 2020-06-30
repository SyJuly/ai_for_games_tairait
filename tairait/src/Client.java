

import AI.BotManager;
import Board.BoardManager;
import lenz.htw.tiarait.ColorChange;
import lenz.htw.tiarait.net.NetworkClient;

import java.io.IOException;

public class Client implements Runnable {
    private String name;
    private boolean isRandom;

    public Client(String name, boolean isRandom){
        this.name = name;
        this.isRandom = isRandom;
    }

    @Override
    public void run() {

        BoardManager boardManager = new BoardManager();
        NetworkClient nc =  new NetworkClient(null, name);

        boardManager.setObstacles(nc);
        BotManager moveDirector = new BotManager(boardManager, isRandom, nc);
        boardManager.setOwnTeam(nc.getMyPlayerNumber());
        moveDirector.setTeam(nc.getMyPlayerNumber()); //0 = rot, 1 = grün, 2=blau, 3=gelb
        System.out.println("Player:  " + name + "--- Team: " + nc.getMyPlayerNumber());
        long start = System.currentTimeMillis();
        boolean printed = false;

        while(nc.isAlive()){
            for(int i = 0; i < 3; i++){
                float x = nc.getX(moveDirector.getTeam(), i);
                float y = nc.getY(moveDirector.getTeam(), i);
                moveDirector.updateBot(i, x, y);
            }

            /*if(!printed && System.currentTimeMillis() - start > 5000){
                start = System.currentTimeMillis();
                boardManager.printBoard(!isRandom);
                printed = false;
            }*/

            for(int i = 0; i < 3; i++){
                float[] direction = moveDirector.getMoveDirection(i);
                //System.out.println("Direction for bot" + i +": " + direction[0] + "|" + direction[1]);
                nc.setMoveDirection(i, direction[0], direction[1]); //rechts oben, V(0,0) bleibt stehen, Länge irrelevant
            }

            ColorChange cc;
            while((cc = nc.getNextColorChange()) != null){
                //cc.newColor; //o=leer; 1-4 spieler //nur einmalig abfragbar
                boardManager.updateBoard(cc.x, cc.y, cc.newColor);
            }

            //boardManager.printScore();
        }

    }

    //spieler werden im mittleren drittel gespawnt
    //space starten
    public static void main(String[] args) throws IOException {
            //new Client("a").run();
            new Thread(new Client("A", false)).start();
            new Thread(new Client("B", true)).start();
            new Thread(new Client("C", true)).start();
            new Thread(new Client("D", true)).start();

    }
}
