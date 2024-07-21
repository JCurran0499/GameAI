package games.tictactoe;

import games.Game2D;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicTacToe extends Game2D<TicTacToe.Square, TicTacToe.Move> {
    private final boolean playerGoesFirst;
    private String winner = null;
    private Square lastPlaced = null;

    public TicTacToe(String playerAgent, boolean playerGoesFirst) {
        super(3, 3, playerAgent, oppositeAgent(playerAgent));
        this.playerGoesFirst = playerGoesFirst;
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
    public String winner() {
        return this.winner;
    }

    @Override
    public String activeAgent() {
        if (this.lastPlaced == null)
            return this.playerGoesFirst ? this.playerAgent : oppositeAgent(this.playerAgent);

        else {
            return oppositeAgent(this.lastPlaced.getAgent());
        }
    }

    @Override
    public TicTacToe move(Move move) {
        if (!moveLegal(move))
            throw new RuntimeException("this move is invalid");

        TicTacToe newGame = this.copy();

        newGame.get(move.x, move.y).setAgent(activeAgent());
        newGame.lastPlaced = newGame.get(move.x, move.y);
        return newGame;
    }

    @Override
    public boolean moveLegal(Move move) {
        String agent = activeAgent();
        return (
            (agent.equals("X") || agent.equals("O")) &&
            !(move.x < 0 || move.x >= 3) || (move.y < 0 || move.y >= 3) &&
            (this.get(move.x, move.y).free())
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
            visualization.append("\n");
            for (Square square : row)
                visualization.append("[").append(square.toString()).append("]");
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
            this.agent = null;
        }

        public boolean free() {
            return this.agent == null;
        }

        public String toString() {
            int squareIndex = (this.getRow() * 3) + this.getCol() + 1;
            return Objects.requireNonNullElse(this.agent, String.valueOf(squareIndex));
        }

        public Square copy() {
            return new Square(this.row, this.col, this.agent);
        }
    }

    public record Move(int x, int y) {}
}
