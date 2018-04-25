package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.controllers.examples.StarterGhosts;

import java.util.*;

/*
 * Using Hill climbing to implement Ms PacMan.
 */

public class HillPacMan extends Controller<MOVE> {

    public MOVE getMove(Game game, long timeDue) {

        MOVE[] possibleMove = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade());
        int bestScore = Integer.MIN_VALUE;
        MOVE bestMove = MOVE.NEUTRAL;
        for (int i = 0; i < possibleMove.length; i++){
            Game copyGame = game.copy();
            MOVE baseMove  = possibleMove[i];
            EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
            for(GHOST ghost : GHOST.values()){
                copyGame.advanceGame(possibleMove[i], ghostMoves);
                int score = copyGame.getScore();
                if( score > bestScore){
                    bestScore = score;
                    bestMove = baseMove;
                }
            }
        }
        return bestMove;
    }
}
