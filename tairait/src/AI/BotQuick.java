package AI;

import Board.Point;
import Board.Team;
import PathFinding.AStar;

import java.util.Collections;
import java.util.List;

public class BotQuick extends Bot {

    public BotQuick() {
        super(1.1f, 0);
    }

    @Override
    public void findNextPath(Clusterer clusterer,
                             AStar pathFinder,
                             List<Point> allFreePoints) {


        List<List<Point>> clusters = clusterer.cluster(allFreePoints);
        List<Point> biggestCluster = clusters.get(0);
        for(int i = 1; i < clusters.size(); i++){
            if(clusters.get(i).size() > biggestCluster.size()){
                biggestCluster = clusters.get(i);
            }
        }
        Collections.sort(biggestCluster, new NearestPointComparator(x,y));
        Point target = biggestCluster.get(biggestCluster.size() - 1);

        int[][] path = pathFinder.AStarSearch((int)x, (int)y,target.x,target.y, true);
        setPath(path);
    }


    //Search free spaces away from other bots

}
