package games.games2d;

import games.Game2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import resources.ConsoleColors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Checkers extends Game2D<Checkers.Square, Checkers.Move> {
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private int redCount;
    private int blackCount;
    private Square lastMove;
    private boolean justSkipped;

    public Checkers(String playerAgent) {
        super(ROWS, COLS, playerAgent, oppositeAgent(playerAgent), playerAgent.equals("B"));
        redCount = blackCount = 12;
        lastMove = null;
        justSkipped = false;
    }

    // Red starts at the top
    // Black starts at the bottom
    protected Square defaultSpace(int r, int c) {
        Square square = new Square(r, c);
        if (
            ((r == 0 || r == 2) && (c % 2 == 1)) ||
            ((r == 1) && (c % 2 == 0))
        ) {
            square.setPiece(new Piece("R")); // RED pieces start at top
        }

        else if (
            ((r == 5 || r == 7) && (c % 2 == 0)) ||
            ((r == 6) && (c % 2 == 1))
        ) {
            square.setPiece(new Piece("B")); // BLACK pieces start at bottom
        }

        return square;
    }

    public Checkers copy() {
        Checkers newGame = new Checkers(this.playerAgent);

        ArrayList<ArrayList<Checkers.Square>> newBoard = new ArrayList<>(this.board.stream().map(
            row -> new ArrayList<>(row.stream().map(Checkers.Square::copy).toList())
        ).toList());

        newGame.winner = this.winner;
        newGame.redCount = this.redCount;
        newGame.blackCount = this.blackCount;
        newGame.lastMove = this.lastMove.copy();
        newGame.justSkipped = this.justSkipped;
        newGame.board = newBoard;
        return newGame;
    }

    // ----- Game Methods ----- \\
    @Override
    public boolean gameOver() {
        if (this.redCount <= 0) {
            this.winner = "B";
            return true;
        }

        else if (this.blackCount <= 0) {
            this.winner = "R";
            return true;
        }

        return false;
    }

    @Override
    public String activeAgent() {
        if (lastMove == null)
            return "B";

        if (justSkipped) {
            for (Direction d : Direction.options(lastMove.piece)) {
                Square adj = adjacent(lastMove, d);
                Square adjSkip = adjacent(adj, d);
                if (adj != null && adjSkip != null &&
                    !adj.free() && !adj.piece.agent.equals(lastMove.piece.agent) && adjSkip.free()
                )
                    return lastMove.piece.agent;
            }
        }

        return oppositeAgent(lastMove.piece.getAgent());
    }

    @Override
    public Checkers move(Move move) {
        if (!moveLegal(move))
            throw new RuntimeException("this move is invalid");

        Square start = this.get(move.getRow(), move.getCol());
        Piece piece = start.piece;

        Square adj = adjacent(start, move.direction);
        Square adjSkip = adjacent(adj, move.direction);
        if (adj.free()) {
            adj.setPiece(start.piece);
            start.removePlayer();

            lastMove = adj;
            justSkipped = false;
        }
        else {
            adjSkip.setPiece(start.piece);
            adj.removePlayer();
            start.removePlayer();

            lastMove = adjSkip;
            justSkipped = true;

            if (piece.agent.equals("R"))
                blackCount--;
            else
                redCount--;

        }

        return this;
    }

    @Override
    public boolean moveLegal(Move move) {
        String agent = activeAgent();
        Square start = this.get(move.getRow(), move.getCol());
        Piece piece = start.getPiece();
        if (!piece.agent.equals(agent))
            return false;

        if (!Arrays.asList(Direction.options(piece)).contains(move.direction))
            return false;

        Square adj = adjacent(start, move.direction);
        Square adjSkip = adjacent(adj, move.direction);
        if (adj == null)
            return false;

        boolean playerJustSkipped = justSkipped && lastMove.piece.agent.equals(agent);
        if (adj.free() && !playerJustSkipped)
            return true;
        else
            return (
                !adj.getPiece().getAgent().equals(start.piece.agent) &&
                adjSkip != null && adjSkip.free()
            );
    }

    @Override
    public List<Move> allMoves() {
        List<Move> moves = new ArrayList<>();
        String agent = this.activeAgent();

        for (ArrayList<Square> row : this.board)
            for (Square square : row)
                if (!square.free() && square.piece.agent.equals(agent))
                    for (Direction d : Direction.options(lastMove.piece)) {
                        Move m = new Move(square, d);
                        if (this.moveLegal(m))
                            moves.add(m);
                    }

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

    private Square adjacent(Square square, Direction direction) {
        if (square == null)
            return null;

        int newRow = square.row + direction.rChange;
        int newCol = square.col + direction.cChange;
        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS)
            return null;

        return this.get(newRow, newCol);
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
    public static class Square implements Serializable {
        private final int row;
        private final int col;
        private Piece piece;

        public Square(int row, int col) {
            this.row = row;
            this.col = col;
            this.piece = null;
        }


        public Square copy() {
            return new Square(this.row, this.col, this.piece.copy());
        }

        public boolean free() {
            return this.piece == null;
        }

        public void removePlayer() {
            if (free())
                throw new RuntimeException("cannot free a space that is already free");

            this.piece = null;
        }

        private static String pieceToString(Piece piece, int index) {
            if (piece == null)
                return String.valueOf(index);

            if (piece.getAgent().equals("B"))
                return ConsoleColors.BLACK + "O" + ConsoleColors.RESET + background(index) + ConsoleColors.BLACK;
            else
                return ConsoleColors.RED + "O" + ConsoleColors.RESET + background(index) + ConsoleColors.BLACK;
        }

        private static String background(int index) {
            if (index % 2 == 0)
                return ConsoleColors.LIGHT_RED_BACKGROUND;
            else
                return ConsoleColors.GRAY_BACKGROUND;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Piece implements Serializable {
        private final String agent;
        private boolean king;

        public Piece(String agent) {
            this.agent = agent;
            king = false;
        }

        public Piece copy() {
            return new Piece(this.agent, this.king);
        }

        public void makeKing() {
            this.king = true;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Move implements Serializable {
        private final int row;
        private final int col;
        private final Direction direction;

        public Move(Square square, Direction direction) {
            this.row = square.getRow();
            this.col = square.getCol();
            this.direction = direction;
        }
    }

    public static class Direction implements Serializable {
        private final int rChange;
        private final int cChange;

        private Direction(int rChange, int cChange) {
            this.rChange = rChange;
            this.cChange = cChange;
        }

        public static final Direction UpLeft = new Direction(-1, -1);
        public static final Direction UpRight = new Direction(-1, 1);
        public static final Direction DownLeft = new Direction(1, -1);
        public static final Direction DownRight = new Direction(1, 1);

        public static Direction[] options(Piece piece) {
            if (piece.isKing())
                return new Direction[] { UpLeft, UpRight, DownLeft, DownRight };

            else if (piece.agent.equals("R"))
                return new Direction[] { DownLeft, DownRight };

            else
                return new Direction[] { UpLeft, UpRight };
        }
    }
}
