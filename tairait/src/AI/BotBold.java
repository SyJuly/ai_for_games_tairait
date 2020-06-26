package AI;

import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Collections;
import java.util.List;

public class BotBold extends Bot {

    //overwrite best and avoid own

    public BotBold() {
        super(0.67f, 2);
    }


    @Override
    public void findNextPath(Clusterer clusterer, AStar pathFinder, List<Point> bestEnemiesPoints) {
        List<List<Point>> clusters = clusterer.cluster(bestEnemiesPoints);
        if(clusters.size() < 1){
            //TODO
        }
        List<Point> biggestCluster = clusters.get(0);
        for(int i = 1; i < clusters.size(); i++){
            if(clusters.get(i).size() > biggestCluster.size()){
                biggestCluster = clusters.get(i);
            }
        }
        Collections.sort(biggestCluster, new NearestPointComparator(x,y));
        Point target = biggestCluster.get(biggestCluster.size() - 1);

        int[][] path = pathFinder.AStarSearch((int)x, (int)y,target.x,target.y, false);
        setPath(path);
    }
}
