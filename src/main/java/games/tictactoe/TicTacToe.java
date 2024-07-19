package games.tictactoe;

import games.Game2D;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TicTacToe extends Game2D<TicTacToe.Square, TicTacToe.Move> {
    private static final int ROWS = 3;
    private static final int COLS = 3;

    private String firstTurnAgent;
    private String winner = null;
    private Square lastPlaced = null;

    public TicTacToe(String playerAgent, String firstTurnAgent) {
        setBoard(ROWS, COLS);
        this.playerAgent = playerAgent;
        this.firstTurnAgent = firstTurnAgent;
    }

    protected Square newSpace(int r, int c) {
        return new Square(r, c);
    }

    protected TicTacToe copy() {
        TicTacToe newGame = new TicTacToe(this.firstTurnAgent, this.winner, this.lastPlaced.copy());

        ArrayList<ArrayList<Square>> newBoard = new ArrayList<>(this.board.stream().map(
            row -> new ArrayList<>(row.stream().map(Square::copy).toList())
        ).toList());

        newGame.playerAgent = this.playerAgent;
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
                Square newPosition = this.lastPlaced.shift(xy.x, xy.y);
                if (newPosition != null && newPosition.getAgent().equals(this.lastPlaced.getAgent()))
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
    public String winner() {
        return this.winner;
    }

    @Override
    public String activeAgent() {
        if (this.lastPlaced == null)
            return this.firstTurnAgent;

        else {
            if (this.lastPlaced.getAgent().equals("X"))
                return "O";
            else
                return "X";
        }
    }

    @Override
    public TicTacToe move(String agent, Move move) {
        if (!moveLegal(agent, move))
            throw new RuntimeException("this move is invalid");

        TicTacToe newGame = this.copy();

        newGame.board.get(move.x).get(move.y).setAgent(agent);
        newGame.lastPlaced = newGame.board.get(move.x).get(move.y);
        return newGame;
    }

    @Override
    public boolean moveLegal(String agent, Move move) {
        return (
            (agent.equals("X") || agent.equals("O")) &&
            !(move.x < 0 || move.x >= 3) || (move.y < 0 || move.y >= 3) &&
            (this.board.get(move.x).get(move.y).free())
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

    public String visualize() {
        StringBuilder visualization = new StringBuilder();
        for (ArrayList<Square> row : this.board) {
            for (Square square : row)
                visualization.append("[").append(square.toString());
            visualization.append("]\n");
        }

        return visualization.toString();
    }

    @Getter
    @Setter
    @Builder(toBuilder = true)
    public class Square {
        private final int row;
        private final int col;
        private String agent;

        public Square(int row, int col) {
            this.row = row;
            this.col = col;
            this.agent = null;
        }

        public boolean free() {
            return this.agent == null;
        }

        public Square shift(int r, int c) {
            if (Square.outOfBounds(r, c))
                return null;

            return board.get(this.row + r).get(this.col + c);
        }

        public String toString() {
            return Objects.requireNonNullElse(this.agent, " ");
        }

        public static boolean outOfBounds(int row, int col) {
            return (row < 0 || row >= 3) || (col < 0 || col >= 3);
        }

        public Square copy() {
            return this.toBuilder().build();
        }
    }

    public record Move(int x, int y) {}
}
