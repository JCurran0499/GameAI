package games;

import java.util.List;

public interface Game<M> {
    boolean gameOver();

    // can assume that gameOver() will always be called before winner()
    String winner();

    String activeAgent();

    Game<M> move(String agent, M move);

    boolean moveLegal(String agent, M move);

    List<M> allMoves();

    double heuristic();
}
