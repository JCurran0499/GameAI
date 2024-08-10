package games;

import lombok.Getter;

import java.util.ArrayList;

public abstract class Game2D<S, M> implements Game<M> {
    protected ArrayList<ArrayList<S>> board;
    protected boolean playerGoesFirst;
    protected String winner;
    @Getter protected final String playerAgent;
    @Getter protected final String botAgent;

    public Game2D(int rows, int cols, String playerAgent, String botAgent, boolean playerGoesFirst) {
        this.winner = null;
        this.playerAgent = playerAgent;
        this.botAgent = botAgent;
        this.playerGoesFirst = playerGoesFirst;
        if (this.playerAgent.equals(this.botAgent))
            throw new RuntimeException("players must have different names");

        this.board = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            this.board.add(new ArrayList<>());

            for (int c = 0; c < cols; c++)
                this.board.get(r).add(defaultSpace(r, c));
        }
    }

    protected abstract S defaultSpace(int r, int c);
    public abstract Game2D<S, M> copy();

    /*
     * Optional override
     * Higher value = better for the bot
     */
    public double heuristic() {
        if (gameOver() && (winner() != null)) {
            if (winner().equals(this.botAgent))
                return Double.MAX_VALUE;
            else
                return Double.MAX_VALUE * -1;
        }

        return 0;
    }

    public S get(int r, int c) {
        try {
            return this.board.get(r).get(c);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public String winner() {
        return this.winner;
    }
}
