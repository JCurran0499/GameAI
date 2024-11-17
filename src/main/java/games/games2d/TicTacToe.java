package games.games2d;

import games.Game2D;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import resources.ConsoleColors;
import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends Game2D<TicTacToe.Square, TicTacToe.Move> {
    private static final int ROWS = 3;
    private static final int COLS = 3;

    private Square lastPlaced;

    public TicTacToe(String playerAgent, boolean playerGoesFirst) {
        super(ROWS, COLS, playerAgent, oppositeAgent(playerAgent), playerGoesFirst);
        this.lastPlaced = null;
    }

    protected Square defaultSpace(int r, int c) {
        return new Square(r, c);
    }

    public TicTacToe copy() {
        TicTacToe newGame = new TicTacToe(this.playerAgent, this.playerGoesFirst);

        ArrayList<ArrayList<Square>> newBoard = new ArrayList<>(this.board.stream().map(
            row -> new ArrayList<>(row.stream().map(Square::copy).toList())
        ).toList());

        newGame.winner = this.winner;
        newGame.lastPlaced = (lastPlaced == null) ? null : lastPlaced.copy();
        newGame.board = newBoard;
        return newGame;
    }

    // ----- Game Methods ----- \\
    @Override
    public boolean gameOver() {
        if (this.lastPlaced == null)
            return false;

        Move[][] lines = new Move[][] {
            new Move[] { new Move(-2, -2), new Move(-1, -1), new Move(1, 1), new Move(2, 2)},
            new Move[] { new Move(2, -2), new Move(1, -1), new Move(-1, 1), new Move(-2, 2)},
            new Move[] { new Move(-2, 0), new Move(-1, 0), new Move(1, 0), new Move(2, 0)},
            new Move[] { new Move(0, -2), new Move(0, -1), new Move(0, 1), new Move(0, 2)}
        };

        int count;
        for (Move[] line : lines) {
            count = 0;
            for (Move xy : line) {
                Square newPosition = get(this.lastPlaced.getRow() + xy.x, this.lastPlaced.getCol()+ xy.y);
                if (newPosition != null && !newPosition.free() && newPosition.getAgent().equals(this.lastPlaced.getAgent()))
                    count += 1;

                if (count >= 2) {
                    this.winner = this.lastPlaced.getAgent();
                    return true;
                }
            }
        }

        return allMoves().isEmpty();
    }

    @Override
    public String activeAgent() {
        if (this.lastPlaced == null)
            return this.playerGoesFirst ? this.playerAgent : this.botAgent;

        else {
            return oppositeAgent(this.lastPlaced.getAgent());
        }
    }

    @Override
    public TicTacToe move(Move move) {
        if (!moveLegal(move))
            throw new RuntimeException("this move is invalid");

        this.get(move.x, move.y).setAgent(this.activeAgent());
        this.lastPlaced = this.get(move.x, move.y);
        return this;
    }

    @Override
    public boolean moveLegal(Move move) {
        return (
            move.x >= 0 && move.x < ROWS && move.y >= 0 && move.y < COLS &&
            this.get(move.x, move.y).free()
        );
    }

    @Override
    public List<Move> allMoves() {
        List<Move> moves = new ArrayList<>();
        for (ArrayList<Square> row : this.board)
            for (Square square : row)
                if (square.free())
                    moves.add(new Move(square.getRow(), square.getCol()));

        return moves;
    }

    @Override
    public String visualize() {
        StringBuilder visualization = new StringBuilder();
        for (ArrayList<Square> row : this.board) {
        visualization.append("\n" + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK);
            for (Square square : row)
                visualization.append("[").append(square.toString()).append("]");
            visualization.append(ConsoleColors.RESET);
        }

        return visualization.toString();
    }

    private static String oppositeAgent(String agent) {
        if (agent.equals("X"))
            return "O";
        else
            return "X";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Square {
        private final int row;
        private final int col;
        private String agent;

        public Square(int row, int col) {
            this.row = row;
            this.col = col;
            this.agent = "";
        }

        public boolean free() {
            return this.agent.isEmpty();
        }

        public String toString() {
            int squareIndex = (this.getRow() * 3) + this.getCol() + 1;
            return agentToString(this.agent, squareIndex);
        }

        public Square copy() {
            return new Square(this.row, this.col, this.agent);
        }

        private static String agentToString(String agent, int index) {
            if (agent.isEmpty())
                return String.valueOf(index);

            if (agent.equals("X"))
                return ConsoleColors.BLUE + agent + ConsoleColors.RESET + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK;
            else
                return ConsoleColors.RED + agent + ConsoleColors.RESET + ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK;
        }
    }

    public record Move(int x, int y) {}
}
