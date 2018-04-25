package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

import java.util.*;

/*
 * Using Interative deepening to implement Ms PacMan.
 */

public class InterativePacMan extends Controller<MOVE> {


    private static final int MIN_DISTANCE = 10;

    public MOVE getMove(Game game, long timeDue) {
        // a deque used to save the state of Node
        Deque<Node> list = new LinkedList<Node>();
        int bestScore = Integer.MIN_VALUE;
        MOVE bestMove = MOVE.NEUTRAL;
        int limit = 20;
        int count = 0;

        //Each time increase the limit (search range)
        while(count <= limit) {
            if (list.isEmpty()) {
                MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
                for (int i = 0; i < possibleMoves.length; i++) {
                    Game copy = game.copy();
                    MOVE baseMove = possibleMoves[i];
                    EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
                    copy.advanceGame(baseMove, ghostMoves);
                    Node temp = new Node(copy, baseMove, 0, baseMove);
                    list.offerFirst(temp);
                }
            }

            while (!list.isEmpty()) {
                Node important = list.pollFirst();
                if (important.depth == count) {
                    int score = important.game.getScore();
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = important.baseMove;
                    }
                } else {
                    MOVE[] possibleMove = important.game.getPossibleMoves(important.game.getPacmanCurrentNodeIndex(), important.game.getPacmanLastMoveMade());

                    for (int i = 0; i < possibleMove.length; i++) {
                        MOVE baseMove = important.baseMove;
                        Game copyGame = important.game.copy();
                        EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
                        copyGame.advanceGame(possibleMove[i], ghostMoves);
                        Node temp = new Node(copyGame, possibleMove[i], important.depth + 1, baseMove);
                        list.offerFirst(temp);
                    }
                }
            }
            count++;
        }
        return bestMove;
    }
}
