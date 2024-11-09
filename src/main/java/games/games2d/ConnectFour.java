package games.games2d;

import games.Game2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import resources.ConsoleColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectFour extends Game2D<ConnectFour.Slot, Integer> {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private int lastPlaced;
    private int[] columns;

    public ConnectFour(String playerAgent, boolean playerGoesFirst) {
        super(ROWS, COLS, playerAgent, oppositeAgent(playerAgent), playerGoesFirst);
        this.lastPlaced = -1;
        this.columns = new int[COLS];
        Arrays.fill(this.columns, ROWS);
    }

    protected Slot defaultSpace(int r, int c) {
        return new Slot(r, c);
    }

    public ConnectFour copy() {
        ConnectFour newGame = new ConnectFour(this.playerAgent, this.playerGoesFirst);

        ArrayList<ArrayList<Slot>> newBoard = new ArrayList<>(this.board.stream().map(
            row -> new ArrayList<>(row.stream().map(Slot::copy).toList())
        ).toList());

        newGame.winner = this.winner;
        newGame.lastPlaced = this.lastPlaced;
        newGame.columns = Arrays.copyOf(this.columns, COLS);
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
        String agent = this.get(r, c).getAgent();

        int[][] streakIncrements = new int[][] {
            {0, 1, 0, -1},
            {1, 0, -1, 0},
            {1, 1, -1, -1},
            {1, -1, -1, 1}
        };

        for (int[] streakInc : streakIncrements) {
            streak = measureStreak(r, c, streakInc[0], streakInc[1]) + measureStreak(r, c, streakInc[2], streakInc[3]);
            if (streak >= 3) {
                this.winner = agent;
                return true;
            }
        }

        return allMoves().isEmpty();
    }

    @Override
    public String activeAgent() {
        if (this.lastPlaced < 0)
            return this.playerGoesFirst ? this.playerAgent : this.botAgent;

        else {
            return oppositeAgent(
                this.get(this.columns[this.lastPlaced], this.lastPlaced).getAgent()
            );
        }
    }

    @Override
    public ConnectFour move(Integer move) {
        if (!moveLegal(move))
            throw new RuntimeException("this move is invalid");

        this.get(columns[move] - 1, move).setAgent(this.activeAgent());
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

        for (ArrayList<Slot> row : this.board) {
            visualization.append("\n" + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK);
            for (Slot slot : row)
                visualization.append("[").append(slot.toString()).append("]");
            visualization.append(ConsoleColors.RESET);
        }

        return visualization.toString();
    }

    private static String oppositeAgent(String agent) {
        if (agent.equals("R"))
            return "B";
        else
            return "R";
    }

    private int measureStreak(int r, int c, int rIncrement, int cIncrement) {
        Slot slot = this.get(r, c);
        String agent = slot.getAgent();
        int streak = 0;

        r += rIncrement;
        c += cIncrement;
        while (
            r >= 0 && r < ROWS && c>= 0 && c < COLS && streak < 3 &&
            slot.getAgent() != null && slot.getAgent().equals(agent)
        ) {
            slot = this.get(r, c);
            if (slot.getAgent() != null && slot.getAgent().equals(agent))
                streak++;

            r += rIncrement;
            c += cIncrement;
        }

        return streak;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Slot {
        private final int row;
        private final int col;
        private String agent;

        public Slot(int row, int col) {
            this.row = row;
            this.col = col;
            this.agent = null;
        }

        public String toString() {
            return agentToString(this.agent);
        }

        public Slot copy() {
            return new Slot(this.row, this.col, this.agent);
        }

        private String agentToString(String agent) {
            if (agent == null)
                return " ";

            if (agent.equals("B"))
                return ConsoleColors.BLUE + "O" + ConsoleColors.RESET + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK;
            else
                return ConsoleColors.RED + "O" + ConsoleColors.RESET + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK;
        }
    }
}
