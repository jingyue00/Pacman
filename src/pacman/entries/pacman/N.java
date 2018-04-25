package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class N
{
    MOVE move;
    Game game;
    MOVE baseMove;
    int h,g;
    int depth;
    N ( Game game, MOVE move, MOVE baseMove,int depth, int h, int g){
        this.move = move;
        this.game = game;
        this.baseMove = baseMove;
        this.depth = depth;
        this.h = h;
        this.g = g;
    }
}