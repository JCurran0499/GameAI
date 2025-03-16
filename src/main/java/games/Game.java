package games;

import java.util.List;

public interface Game<M> {
    // it is client responsibility to verify a move is legal before calling this method
    Game<M> move(M move);

    List<M> allMoves();

    boolean moveLegal(M move);

    boolean gameOver();

    // can assume that gameOver() will always be called before winner()
    String winner();

    Game<M> copy();

    String activePlayer();

    double heuristic(String bot);
}
