package games.games2d;

import games.Game2D;
import resources.PlayerTypes;
import resources.ConsoleColors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToe extends Game2D<String, TicTacToe.Move> {
    private static final int ROWS = 3;
    private static final int COLS = 3;

    private Move lastPlaced;
    
    // generates copy board
    private TicTacToe(TicTacToe ttt) {
        super(null, ttt.winner, ttt.playerTypes, ttt.player1, ttt.player2);
        this.lastPlaced = ttt.lastPlaced;
    }

    public TicTacToe(String player1) {
        super(ROWS, COLS, PlayerTypes.TIC_TAC_TOE, player1, (r, c) -> "");
        this.lastPlaced = null;
    }

    public TicTacToe copy() {
        TicTacToe newGame = new TicTacToe(this);

        String[][] newBoard = new String[ROWS][];
        for (int i = 0; i < ROWS; i++)
            newBoard[i] = Arrays.copyOf(this.board[i], COLS);

        newGame.board = newBoard;
        return newGame;
    }

    // ----- Game Methods ----- \\
    @Override
    public boolean gameOver() {
        if (this.lastPlaced == null)
            return false;

        int[][][] lines = new int[][][] {
            new int[][] { new int[]{-2, -2}, new int[]{-1, -1}, new int[]{1, 1}, new int[]{2, 2} },
            new int[][] { new int[]{2, -2}, new int[]{1, -1}, new int[]{-1, 1}, new int[]{-2, 2} },
            new int[][] { new int[]{-2, 0}, new int[]{-1, 0}, new int[]{1, 0}, new int[]{2, 0} },
            new int[][] { new int[]{0, -2}, new int[]{0, -1}, new int[]{0, 1}, new int[]{0, 2} }
        };

        String last = get(this.lastPlaced.r(), this.lastPlaced.c());
        int count;
        for (int[][] line : lines) {
            count = 0;
            for (int[] xy : line) {
                String point = get(this.lastPlaced.r() + xy[0], this.lastPlaced.c() + xy[1]);
                if (point != null && point.equals(last))
                    count += 1;

                if (count >= 2) {
                    this.winner = last;
                    return true;
                }
            }
        }

        return allMoves().isEmpty();
    }

    @Override
    public String activePlayer() {
        if (this.lastPlaced == null)
            return this.player1;

        else {
            return this.playerTypes.opposite(
                get(this.lastPlaced.r(), this.lastPlaced.c())
            );
        }
    }

    @Override
    public TicTacToe move(Move move) {
        set(move.r(), move.c(), activePlayer());
        this.lastPlaced = move;
        return this;
    }

    @Override
    public boolean moveLegal(Move move) {
        if (move == null)
            return false;

        String space = get(move.r(), move.c());
        return (space != null && space.isEmpty());
    }

    @Override
    public List<Move> allMoves() {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (get(r, c).isEmpty())
                    moves.add(new Move(r, c));

        return moves;
    }

    @Override
    public String visualize() {
        StringBuilder visualization = new StringBuilder();
        for (int r = 0; r < ROWS; r++) {
        visualization.append("\n" + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK);
            for (int c = 0; c < COLS; c++) {
                visualization.append("[").append(
                    visualizeSpace(get(r, c), r, c)
                ).append("]");
            }
            visualization.append(ConsoleColors.RESET);
        }

        return visualization.toString();
    }
    
    private String visualizeSpace(String player, int row, int col) {
        if (player.isEmpty())
            return String.valueOf((row * 3) + col + 1);

        if (player.equals("X"))
            return ConsoleColors.BLUE + player + ConsoleColors.RESET + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK;
        else
            return ConsoleColors.RED + player + ConsoleColors.RESET + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK;
    }

    public record Move(int r, int c) {}
}
