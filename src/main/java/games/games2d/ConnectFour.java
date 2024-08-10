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
        newGame.lastPlaced = lastPlaced;
        newGame.columns = Arrays.copyOf(this.columns, COLS);
        newGame.board = newBoard;
        return newGame;
    }

    // ----- Game Methods ----- \\
    @Override
    public boolean gameOver() {
        int streak = 0;
        int r = this.columns[this.lastPlaced];
        int c = this.lastPlaced;
        Slot slot = this.get(this.columns[this.lastPlaced], this.lastPlaced);

        return true;
    }

    @Override
    public String activeAgent() {
        if (this.lastPlaced < 0)
            return this.playerGoesFirst ? this.playerAgent : this.botAgent;

        else {
            return oppositeAgent(
                this.board.get(this.columns[this.lastPlaced]).get(this.lastPlaced).getAgent()
            );
        }
    }

    @Override
    public ConnectFour move(Integer move) {
        if (!moveLegal(move))
            throw new RuntimeException("this move is invalid");

        ConnectFour newGame = this.copy();

        newGame.columns[move]--;
        newGame.get(this.columns[move], move).setAgent(activeAgent());
        newGame.lastPlaced = move;
        return newGame;
    }

    @Override
    public boolean moveLegal(Integer move) {
        return (move >= 0 && move < 7 && this.columns[move] > 0);
    }

    @Override
    public List<Integer> allMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            if (this.columns[i] > 0)
                moves.add(i);

        return moves;
    }

    @Override
    public String visualize() {
        StringBuilder visualization = new StringBuilder();
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
