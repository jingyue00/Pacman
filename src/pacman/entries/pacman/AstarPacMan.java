package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

import java.util.*;

/*
 * Using A* to implement Ms PacMan.
 */

public class AstarPacMan extends Controller<MOVE> {

    public MOVE getMove(Game game, long timeDue) {
        int root = game.getPacmanCurrentNodeIndex();
        MOVE bestMove = MOVE.NEUTRAL;
        int limit = 20;
        int bestScore = Integer.MIN_VALUE;
        // a priority queue used to save the state of Node
        PriorityQueue<N> list = new PriorityQueue<N>(limit, new Comparator<N>(){
            @Override
            public int compare(N o1, N o2){
                if (o1.equals(o2)) {
                    return 0;
                }
                return o1.game.getScore() > o2.game.getScore() ? -1 : 1;
            }
        });

        int[] pills=game.getPillIndices();
        int[] powerPills=game.getPowerPillIndices();

        ArrayList<Integer> targets=new ArrayList<Integer>();

        for(int i=0;i<pills.length;i++)
            if(game.isPillStillAvailable(i))
                targets.add(pills[i]);

        for(int i=0;i<powerPills.length;i++)
            if(game.isPowerPillStillAvailable(i))
                targets.add(powerPills[i]);

        int[] targetsArray=new int[targets.size()];

        for(int i=0;i<targetsArray.length;i++)
            targetsArray[i]=targets.get(i);

        MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade());
        for (int i = 0; i < possibleMoves.length; i++) {
            Game copyGame = game.copy();
            MOVE baseMove  = possibleMoves[i];
            EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
            copyGame.advanceGame(baseMove, ghostMoves);
            int current  = copyGame.getPacmanCurrentNodeIndex();
            int h = game.getManhattanDistance(root, current);
            int g = game.getShortestPathDistance(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray, Constants.DM.PATH));
            N temp = new N( copyGame, baseMove, baseMove, 0, h, g);
            list.add(temp);
        }

        while (!list.isEmpty()) {
            N important = list.poll();
            if(important.depth == limit){
                int score = important.game.getScore();
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = important.baseMove;
                }
            } else {
                MOVE[] possibleMove = important.game.getPossibleMoves(important.game.getPacmanCurrentNodeIndex(),important.game.getPacmanLastMoveMade());

                for (int i = 0; i < possibleMove.length; i++) {
                    MOVE baseMove = important.baseMove;
                    Game copyGame = important.game.copy();
                    EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
                    copyGame.advanceGame(possibleMove[i], ghostMoves);
                    int current  = copyGame.getPacmanCurrentNodeIndex();
                    int h = game.getManhattanDistance(root, current);
                    int g = game.getShortestPathDistance(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray, Constants.DM.PATH));
                    N temp = new N( copyGame, possibleMove[i], baseMove, important.depth + 1, h , g);
                    list.add(temp);
                }
            }
        }

        return bestMove;
    }
}

