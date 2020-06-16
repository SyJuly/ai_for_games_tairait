

import lenz.htw.tiarait.ColorChange;
import lenz.htw.tiarait.net.NetworkClient;

import java.io.IOException;

public class Client {

    //spieler werden im mittleren drittel gespawnt
    //space starten
    public static void main(String[] args) throws IOException {
        BoardManager boardManager = new BoardManager();
        NetworkClient nc =  new NetworkClient(null, "teamName");
        int playerNumber = nc.getMyPlayerNumber(); //0 = rot, 1 = grün, 2=blau, 3=gelb

        int counter = 0;
        for(int i = 0; i < boardManager.board.length; i++){
            for(int j = 0; j < boardManager.board[i].length; j++){
                if(nc.isWall(i,j)){
                    boardManager.setObstacle(i, j);
                    counter++;
                    System.out.println("found obstacle: " + counter);
                }
            }
        }
        while(nc.isAlive()){
            //steuerung
            ;
            int botNr = 0; //0-3
            float x = nc.getX(playerNumber, botNr);
            float y = nc.getY(playerNumber, botNr);
            //Brett ganzzahlig also int -> 0 - 31
            nc.isWall(0,1); //nur der kreis
            nc.setMoveDirection(0, 1,1); //rechts oben, V(0,0) bleibt stehen, Länge irrelevant
            nc.setMoveDirection(1, 1,1);
            nc.setMoveDirection(2, 1,1);
            // Latenzen
            // kein Bremsen, keine Beschleunigung
            ColorChange cc;
            /*while((cc=nc.getNextColorChange()) != null){
                //liste, iterator
                //cc in eigene struktur einarbeiten
                //cc.newColor; //o=leer; 1-4 spieler //nur einmalig abfragbar
                //punkte zählen
            }*/
        }

    }
}
