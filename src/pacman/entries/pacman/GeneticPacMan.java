package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

import java.util.*;

/*
 * Using Genetic algorithm to implement Ms PacMan.
 */

public class GeneticPacMan extends Controller<MOVE> {
    private static final MOVE[] moves = {MOVE.LEFT, MOVE.DOWN, MOVE.NEUTRAL, MOVE.RIGHT, MOVE.UP};

    public MOVE getMove(Game game, long timeDue) {

        MOVE[] possibleMove = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade());
        int bestScore = Integer.MIN_VALUE;
        MOVE bestMove = MOVE.NEUTRAL;

        possibleMove = generate(possibleMove,5);
        possibleMove = crossover(possibleMove,0);
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

    //Generate random decendants
    public MOVE[] generate(MOVE[] list, int listLength){
        MOVE [] newlist = new MOVE[list.length + listLength];
        for (int i = 0; i<list.length; i++){
            newlist[i] = list[i];
        }
        for (int j = 0; j < listLength; j++){
            int random = (int)(Math.random()*4);
            MOVE move = moves[random];
            newlist[list.length + j] = move;
        }
        return newlist;
    }

    //Cross over random decendants
    public MOVE[] crossover(MOVE[] list, int count){
        double random = Math.random();
        MOVE[] newList = new MOVE[list.length-count];
        if(list.length >= count){
            if(random < 0.5){
                for(int i = 0; i < list.length - count; i++){
                    newList[i] = list[i];
                }
            }
            else{
                for(int i = count; i < list.length-1; i++){
                    newList[i] = list[i];
                }
            }
        }
        return newList;
    }
}
