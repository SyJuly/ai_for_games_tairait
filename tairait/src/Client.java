

import lenz.htw.tiarait.ColorChange;
import lenz.htw.tiarait.net.NetworkClient;

import java.io.IOException;

public class Client implements Runnable {
    private String name;

    public Client(String name){
        this.name = name;
    }

    @Override
    public void run() {
        BoardManager boardManager = new BoardManager();
        MoveDirector moveDirector = new MoveDirector(boardManager);
        NetworkClient nc =  new NetworkClient(null, name);

        moveDirector.setTeam(nc.getMyPlayerNumber()); //0 = rot, 1 = grün, 2=blau, 3=gelb
        boardManager.setObstacles(nc);

        while(nc.isAlive()){
            int botNr = 0; //0-3
            float x = nc.getX(moveDirector.getTeam(), botNr);
            float y = nc.getY(moveDirector.getTeam(), botNr);

            for(int i = 0; i < 3; i++){
                int[] direction = moveDirector.getMoveDirection(i);
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

            new Thread(new Client("A")).start();
            //new Thread(new Client("B")).start();
            //new Thread(new Client("C")).start();
            //new Thread(new Client("D")).start();

    }
}
