package AI;

import Board.BoardManager;
import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Collections;
import java.util.List;

public class BotQuick extends Bot {

    public BotQuick(BotManager botManager) {
        super(botManager,1.1f, 1);
    }

    @Override
    public void findNextPath(List<Point> allFreePoints) {
        if(allFreePoints.size() < 1){
            findRandomPath();
            return;
        }

        List<List<Point>> clusters = botManager.getClusterer().cluster(allFreePoints);
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


        int[][] path = botManager.getPathFinder().AStarSearch((int)x, (int)y,target.x,target.y, true);
        if(path == null){
            findRandomPath();
            return;
        }
        setPath(path);
    }


    //Search free spaces away from other bots

}
