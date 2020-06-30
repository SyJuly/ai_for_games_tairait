package AI;

import AI.Bot;
import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotNasty extends Bot {

    //Erease next enemy cluster and avoid own spaces

    public BotNasty(BotManager botManager, BotManagerAssistent assistent) {
        super(botManager, assistent,1, 0);
    }


    @Override
    public void findNextPath(List<Point> allEnemiesPoints) {
        if(allEnemiesPoints.size() < 1){
            return;
        }
        List<List<Point>> clusters = assistent.getPossessedPointClusters();
        Point target;

        if(clusters.size() < 1){
            target = getClosestTargetNotSelf(allEnemiesPoints);
        } else {
            List<Point> nearestCluster = getNearestCluster(clusters);
            //System.out.println("Position: "+ x+","+y+"----Sorted possessed points: " + Arrays.toString(nearestCluster.toArray()));
            target = getTargetWithMaxDistance(nearestCluster);
            if(target.isPoint((int)x,(int)y)){
                return;
            }
        }

        int[][] path = botManager.getPath((int)x, (int)y,target, botCode);
        if(path != null && path.length < 2){
            System.out.println("Nasty bot did somethin wrong. Path from: " + (int)x +","+ (int)y + " to " +target);
            findRandomPath();
            return;
        }
        if(path == null){
            System.out.println("Nasty bot does somthing random, because no path was found.");
            findRandomPath();
            return;
        }
        setPath(path);
    }

}
