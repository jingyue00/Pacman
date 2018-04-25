package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE> {


	public MOVE getMove(Game game, long timeDue) {
		int limit = 20;
		Deque<Node> nodeSequence= new LinkedList<Node>();
		//get possible Moves in MOVE[];
		MOVE[] moveSeqence = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		if (nodeSequence.isEmpty()) {
			for (int i = 0; i < moveSeqence.length; i++) {
				Game copy = game.copy();
				EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
				copy.advanceGame(moveSeqence[i], ghostMoves);
				Node temp = new Node( copy,moveSeqence[i], 0, moveSeqence[i]);
				nodeSequence.offerFirst(temp);
			}
		}



		//two various to return best solution
		int bestScore = Integer.MIN_VALUE;
		MOVE finalFirstMove = MOVE.NEUTRAL;
		//int depth = 0;
		while (!nodeSequence.isEmpty()) {
			Node solution = nodeSequence.pollFirst();
			if(solution.depth == limit){
				int score = solution.game.getScore();
				if (bestScore < score) {
					bestScore = score;
					finalFirstMove = solution.baseMove;
				}
			} else {
				MOVE[] possibleMove = solution.game.getPossibleMoves(solution.game.getPacmanCurrentNodeIndex(),solution.game.getPacmanLastMoveMade());

				for (int i = 0; i < possibleMove.length; i++) {
					EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
					Game solutionChild = solution.game.copy();
					solutionChild.advanceGame(solution.move, ghostMoves);
					Node temp = new Node( solutionChild, possibleMove[i],solution.depth + 1, solution.baseMove);
					nodeSequence.offerFirst(temp);
				}
			}
		}
		return finalFirstMove;
	}
}