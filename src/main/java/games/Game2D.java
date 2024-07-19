package games;

import java.util.ArrayList;

public abstract class Game2D<S, M> implements Game<M> {
    protected ArrayList<ArrayList<S>> board = null;
    protected String playerAgent = null;

    protected void setBoard(int rows, int cols) {
        board = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            board.add(new ArrayList<>());

            for (int c = 0; c < cols; c++)
                board.get(r).add(newSpace(r, c));
        }
    }

    protected abstract S newSpace(int r, int c);
    public abstract Game2D<S, M> copy();

    /*
     * Optional override
     */
    public double heuristic() {
        if (gameOver() && (winner() == null)) {
            if (winner().equals(playerAgent))
                return Double.MAX_VALUE;
            else
                return Double.MIN_VALUE;
        }

        return 0;
    }

    public S get(int r, int c) {
        return this.board.get(r).get(c);
    }
}
