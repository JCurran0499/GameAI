package games.games2d;

import games.Game2D;
import resources.ConsoleColors;
import resources.Players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectFour extends Game2D<String, Integer> {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private int lastPlaced;
    private final int[] columns; // for each column, the lowest row index with piece

    // generates copy board
    private ConnectFour(ConnectFour c4) {
        super(null, c4.winner, c4.players);
        this.lastPlaced = c4.lastPlaced;
        this.columns = Arrays.copyOf(c4.columns, COLS);
    }

    public ConnectFour() {
        super(ROWS, COLS, Players.CONNECT_FOUR, (r, c) -> "");
        this.lastPlaced = -1;
        this.columns = new int[COLS];
        Arrays.fill(this.columns, ROWS);
    }

    public ConnectFour copy() {
        ConnectFour newGame = new ConnectFour(this);

        String[][] newBoard = new String[ROWS][];
        for (int i = 0; i < ROWS; i++)
            newBoard[i] = Arrays.copyOf(this.board[i], COLS);

        newGame.board = newBoard;
        return newGame;
    }

    // ----- Game Methods ----- \\
    @Override
    public boolean gameOver() {
        if (this.lastPlaced < 0)
            return false;

        int streak;
        int r = this.columns[this.lastPlaced];
        int c = this.lastPlaced;
        String player = get(r, c);

        int[][] streakIncrements = new int[][] {
            {0, 1, 0, -1},
            {1, 0, -1, 0},
            {1, 1, -1, -1},
            {1, -1, -1, 1}
        };

        for (int[] streakInc : streakIncrements) {
            streak = measureStreak(r, c, streakInc[0], streakInc[1]) + measureStreak(r, c, streakInc[2], streakInc[3]);
            if (streak >= 3) {
                this.winner = player;
                return true;
            }
        }

        return allMoves().isEmpty();
    }

    @Override
    public String activePlayer() {
        if (this.lastPlaced < 0)
            return this.players.player1();

        else {
            return this.players.opposite(
                get(this.columns[this.lastPlaced], this.lastPlaced)
            );
        }
    }

    @Override
    public ConnectFour move(Integer move) {
        set(columns[move] - 1, move, activePlayer());
        columns[move] = columns[move] - 1;
        lastPlaced = move;
        return this;
    }

    @Override
    public boolean moveLegal(Integer move) {
        return (move >= 0 && move < COLS && this.columns[move] > 0);
    }

    @Override
    public List<Integer> allMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < this.columns.length; i++)
            if (this.columns[i] > 0)
                moves.add(i);

        return moves;
    }

    @Override
    public String visualize() {
        StringBuilder visualization = new StringBuilder();
        for (int i = 1; i <= COLS; i++)
            visualization.append(" ").append(i).append(" ");
        visualization.append(" ");

        for (String[] row : this.board) {
            visualization.append("\n" + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK);
            for (String slot : row)
                visualization.append("[").append(
                    visualizeSlot(slot)
                ).append("]");
            visualization.append(ConsoleColors.RESET);
        }

        return visualization.toString();
    }

    private String visualizeSlot(String player) {
        if (player.isEmpty())
            return " ";

        if (player.equals("B"))
            return ConsoleColors.BLUE + "O" + ConsoleColors.RESET + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK;
        else
            return ConsoleColors.RED + "O" + ConsoleColors.RESET + ConsoleColors.GRAY_BACKGROUND + ConsoleColors.BLACK;
    }

    private int measureStreak(int r, int c, int rIncrement, int cIncrement) {
        String player, temp;
        player = temp = get(r, c);
        int streak = 0;

        r += rIncrement;
        c += cIncrement;
        while (
            temp != null && temp.equals(player)
        ) {
            temp = get(r, c);
            if (temp != null && temp.equals(player))
                streak++;

            r += rIncrement;
            c += cIncrement;
        }

        return streak;
    }
}
