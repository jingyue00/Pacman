package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Node  {
    MOVE move;
    Game game;
    MOVE baseMove;
    int depth;
    Node ( Game game, MOVE move, int depth, MOVE baseMove){
        this.move = move;
        this.game = game;
        this.baseMove = baseMove;
        this.depth = depth;
    }

    Node(MOVE move, Game game, MOVE baseMove, int depth) {
        this.move = move;
        this.game = game;
        this.baseMove = baseMove;
        this.depth = depth;
    }
}
