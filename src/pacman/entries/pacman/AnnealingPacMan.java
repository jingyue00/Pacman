package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

import java.util.*;

/*
 * Using Simulated Annealing to implement Ms PacMan.
 */

public class AnnealingPacMan extends Controller<MOVE> {

    private static final int MIN_DISTANCE = 10;

    public MOVE getMove(Game game, long timeDue) {

        Deque<Node> list = new LinkedList<Node>();
        int limit = 50;

        MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade());

        //When the list is empty, save all next step with state, depth and baseMove in the list
        for (int i = 0; i < possibleMoves.length; i++) {
            Game copyGame = game.copy();
            MOVE baseMove  = possibleMoves[i];
            EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
            copyGame.advanceGame(baseMove, ghostMoves);
            Node temp = new Node( copyGame, baseMove, 0, baseMove);
            list.offerFirst(temp);
        }

        //If the depth of positions smaller then limit
        while(list.peekLast().depth < limit) {
            Node target = list.pollLast();
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

        Random random = new Random();
        Node rootNode = getNode(list, random.nextInt(list.size()));
        int bestScore = rootNode.game.getScore();
        MOVE bestMove = rootNode.baseMove;
        int moveLimit = 5;
        int moveTime = 0;
        // If the time of move smaller then limit
        while( moveTime < moveLimit){
            Node target = getNode(list, random.nextInt(list.size()));
            if(bestScore <= target.game.getScore()){
                bestScore = target.game.getScore();
                bestMove = target.baseMove;
                moveTime++;}
            else{
                Random newRandom = new Random();
                double possibility = newRandom.nextDouble();
                double myPossibility = Math.pow(Math.E, (target.game.getScore()-rootNode.game.getScore())/rootNode.game.getScore());
                if( possibility > myPossibility){
                    bestScore = target.game.getScore();
                    bestMove = target.baseMove;
                    moveTime++;
                }
            }
        }
        return bestMove;
    }

    private Node getNode(Deque<Node> list, int target){
        Deque<Node> newlist = new LinkedList<Node>();
        Node newNode = null;
        for(int i = 0; i <= target; i++){
            if(i == target){
                newNode = list.peekFirst();
            }
            newlist.offerFirst(list.pollFirst());
        }
        for(int i = 0; i <= target; i++){
            list.offerFirst(newlist.pollFirst());
        }
        return newNode;
    }
}
