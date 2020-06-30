package AI;

import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BotBold extends Bot {

    //overwrite best and avoid own

    public BotBold(BotManager botManager) {
        super(botManager, 0.67f, 2);
    }


    @Override
    public void findNextPath(List<Point> bestEnemiesPoints) {
        if(bestEnemiesPoints.size() < 1){
            findRandomPath();
            return;
        }
        List<List<Point>> clusters = botManager.getClusterer().cluster(bestEnemiesPoints);
        if(clusters.size() < 1){
            findRandomPath();
            return;
        }
        List<Point> biggestCluster = clusters.get(0);
        for(int i = 1; i < clusters.size(); i++){
            if(clusters.get(i).size() > biggestCluster.size()){
                biggestCluster = clusters.get(i);
            }
        }
        Collections.sort(biggestCluster, new NearestPointComparator(x,y));
        Point target = biggestCluster.get(biggestCluster.size() - 1);
        //System.out.println("Bold one targets: " + target + " from " + clusters.size() + " clusters.");

        int[][] path = botManager.getPath((int)x, (int)y,target, botCode);
        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }
}
