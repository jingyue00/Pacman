package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

import java.util.*;

/*
 * Using Breadth-first search to implement Ms PacMan
 */

public class BFSPacMan extends Controller<MOVE> {


    private static final int MIN_DISTANCE = 10;

    public MOVE getMove(Game game, long timeDue) {
        // a deque used to save the state of Node
        Deque<Node> list = new LinkedList<Node>();
        int bestScore = Integer.MIN_VALUE;
        MOVE bestMove = MOVE.NEUTRAL;
        int limit = 30;

        //If the list is empty, save all next step with state, depth and baseMove in the list
        if(list.isEmpty()){
            MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade());
            for (int i = 0; i < possibleMoves.length; i++) {
                Game copyGame = game.copy();
                MOVE baseMove  = possibleMoves[i];
                EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
                copyGame.advanceGame(baseMove, ghostMoves);
                Node temp = new Node( copyGame,baseMove, 0, baseMove);
                list.offerFirst(temp);
            }
        }

        //If the list is not empty, poll out the last one. Find its child and save them in the list.
        while (!list.isEmpty() && list.peekLast().depth<= limit) {
            Node target = list.pollLast();
            if(target.depth == limit){
                int score = target.game.getScore();
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = target.baseMove;
                }
            } else {
                MOVE[] possibleMove = target.game.getPossibleMoves(target.game.getPacmanCurrentNodeIndex(),target.game.getPacmanLastMoveMade());

                for (int i = 0; i < possibleMove.length; i++) {
                    MOVE baseMove = target.baseMove;
                    Game copyGame = target.game.copy();
                    EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
                    copyGame.advanceGame(possibleMove[i], ghostMoves);
                    Node temp = new Node( copyGame, possibleMove[i], target.depth + 1, baseMove);
                    list.offerFirst(temp);
                }
            }
        }
        return bestMove;
    }
}
