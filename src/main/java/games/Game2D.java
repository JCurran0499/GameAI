package games;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import resources.PlayerTypes;

import java.lang.reflect.Array;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Game2D<S, M> implements Game<M> {
    public interface NewSquare<S> {
        S call(int r, int c);
    }

    protected S[][] board;
    protected String winner;
    protected final PlayerTypes playerTypes;
    @Getter protected final String player1;
    @Getter protected final String player2;

    @SuppressWarnings("unchecked")
    public Game2D(int rows, int cols, PlayerTypes playerTypes, String player1, NewSquare<S> f) {
        if (!playerTypes.validType(player1))
            throw new RuntimeException("invalid player configuration");

        this.winner = null;
        this.playerTypes = playerTypes;
        this.player1 = player1;
        this.player2 = playerTypes.opposite(player1);

        this.board = (S[][]) Array.newInstance(f.call(0, 0).getClass(), rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++)
                this.board[r][c] = f.call(r, c);
        }
    }

    public S get(int r, int c) {
        if (r < 0 || r >= this.board.length || c < 0 || c >= this.board[0].length)
            return null;

        return this.board[r][c];
    }

    public void set(int r, int c, S s) {
        this.board[r][c] = s;
    }

    // enforces return type of move() to the more specific type
    public abstract Game2D<S, M> move(M move);

    public abstract String visualize();

    /*
     * Optional override
     * Higher value = better for the bot
     */
    public double heuristic(String bot) {
        if (gameOver() && (winner() != null)) {
            if (winner().equals(bot))
                return Double.MAX_VALUE;
            else
                return Double.MAX_VALUE * -1;
        }

        return 0;
    }

    public String winner() {
        return this.winner;
    }
}
