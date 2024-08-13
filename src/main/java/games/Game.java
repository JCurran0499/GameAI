package games;

import java.util.List;

public interface Game<M> {
    String getBotAgent();

    boolean gameOver();

    // can assume that gameOver() will always be called before winner()
    String winner();

    String activeAgent();

    Game<M> move(M move);

    boolean moveLegal(M move);

    List<M> allMoves();

    double heuristic();
}
