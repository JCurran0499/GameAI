package games.games2d;

import games.Game2D;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import resources.ConsoleColors;
import resources.PlayerTypes;

import java.util.ArrayList;
import java.util.List;

public class Checkers extends Game2D<Checkers.Piece, Checkers.Move> {
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private int redCount;
    private int blackCount;
    private Move lastMove;
    private String justSkipped;

    private Checkers(Checkers c) {
        super(null, c.winner, c.playerTypes, c.player1, c.player2);
        this.redCount = c.redCount;
        this.blackCount = c.blackCount;
        this.lastMove = c.lastMove;
        this.justSkipped = c.justSkipped;
    }

    public Checkers(String player1) {
        super(ROWS, COLS, PlayerTypes.CHECKERS, player1, Checkers::newPiece);
        this.redCount = this.blackCount = 12;
        this.lastMove = null;
        this.justSkipped = "";
    }

    // Red starts at the top
    // Black starts at the bottom
    private static Piece newPiece(int r, int c) {
        if (
            ((r == 0 || r == 2) && (c % 2 == 1)) ||
            ((r == 1) && (c % 2 == 0))
        ) {
            return new Piece("R"); // RED pieces start at top
        }

        else if (
            ((r == 5 || r == 7) && (c % 2 == 0)) ||
            ((r == 6) && (c % 2 == 1))
        ) {
            return new Piece("B"); // BLACK pieces start at bottom
        }

        return Piece.NULL;
    }

    public Checkers copy() {
        Checkers newGame = new Checkers(this);

        Piece[][] newBoard = new Piece[ROWS][];
        for (int r = 0; r < ROWS; r++) {
            newBoard[r] = new Piece[COLS];
            for (int c = 0; c < COLS; c++)
                newBoard[r][c] = this.board[r][c].copy();
        }

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
    public String activePlayer() {
        if (this.lastMove == null)
            return this.player1;

        Piece piece = get(this.lastMove.dstRow(), this.lastMove.dstCol());
        if (this.justSkipped.equals(piece.getPlayer())) {
            List<Move> opts = options(this.lastMove.dstRow(), this.lastMove.dstCol());
            for (Move opt : opts) {
                if (opt.isSkip())
                    return piece.getPlayer();
            }
        }

        return this.playerTypes.opposite(piece.getPlayer());
    }

    @Override
    public Checkers move(Move move) {
        Piece piece = get(move.srcRow(), move.srcCol());
        if (move.isSkip()) {
            int targetRow = (move.srcRow() + move.dstRow()) / 2;
            int targetCol = (move.srcCol() + move.dstCol()) / 2;
            if (get(targetRow, targetCol).getPlayer().equals("R"))
                redCount--;
            else
                blackCount--;

            set(targetRow, targetCol, Piece.NULL);
            this.justSkipped = piece.getPlayer();
        }
        else {
            this.justSkipped = "";
        }

        set(move.srcRow(), move.srcCol(), Piece.NULL);
        set(move.dstRow(), move.dstCol(), piece);
        if (
            move.dstRow() == 0 && piece.getPlayer().equals("B") ||
                move.dstRow() + 1 == ROWS && piece.getPlayer().equals("R")
        )
            piece.makeKing();
        this.lastMove = move;

        return this;
    }

    @Override
    public boolean moveLegal(Move move) {
        if (!move.isValid())
            return false;

        String player = activePlayer();
        Piece piece = get(move.srcRow(), move.srcCol());
        if (piece == null || !piece.getPlayer().equals(player))
            return false;

        List<Move> opts = options(move.srcRow(), move.srcCol());
        for (Move m : opts) {
            if (move.equals(m))
                return true;
        }

        return false;
    }

    @Override
    public List<Move> allMoves() {
        List<Move> moves = new ArrayList<>();
        String player = this.activePlayer();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece piece = get(r, c);
                if (!piece.isNull() && piece.getPlayer().equals(player)) {
                    moves.addAll(
                        options(r, c)
                    );
                }
            }
        }

        return moves;
    }

    @Override
    public double heuristic(String bot) {
        if (bot.equals("R"))
            return this.redCount - this.blackCount;
        else
            return this.blackCount - this.redCount;
    }

    @Override
    public String visualize() {
        StringBuilder visualization = new StringBuilder("  ");
        char colChar = 'A';
        for (int col = 0; col < COLS; col++)
            visualization.append(" ").append(colChar++).append(" ");

        for (int r = 0; r < ROWS; r++) {
            visualization.append("\n").append(r + 1).append(" ").append(ConsoleColors.BLACK);
            for (int c = 0; c < COLS; c++) {
                Piece piece = get(r, c);
                visualization.append(piece.toString(r + c));
            }
            visualization.append(ConsoleColors.RESET);
        }

        return visualization.toString();
    }

    // all the valid moves for a piece at a given location
    private List<Move> options(int r, int c) {
        Piece piece = get(r, c);
        if (piece == null)
            throw new RuntimeException("invalid board position");

        int[][] allShifts;
        if (piece.isKing())
            allShifts = new int[][]{new int[]{-1, -1}, new int[]{-1, 1}, new int[]{1, -1}, new int[]{1, 1}};
        else if (piece.getPlayer().equals("R"))
            allShifts = new int[][]{new int[]{1, -1}, new int[]{1, 1}};
        else
            allShifts = new int[][]{new int[]{-1, -1}, new int[]{-1, 1}};

        List<Move> moveOpts = new ArrayList<>();
        for (int[] shift : allShifts) {
            Piece adj = get(r + shift[0], c + shift[1]);
            if (adj != null && adj.isNull()) { // free space immediately adjacent
                if (!this.justSkipped.equals(piece.getPlayer()))
                    moveOpts.add(new Move(r, c, r + shift[0], c + shift[1]));
            }
            else if (adj != null && !adj.getPlayer().equals(piece.getPlayer())) { // opponent immediately adjacent
                Piece adjSkip = get(r + (shift[0]*2), c + (shift[1]*2));
                if (adjSkip != null && adjSkip.isNull())
                    moveOpts.add(new Move(r, c, r+(shift[0]*2), c+(shift[1]*2)));
            }
        }

        return moveOpts;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Piece {
        private final String player;
        private boolean king;

        private Piece() {
            this.player = "";
            this.king = false;
        }

        public Piece(String player) {
            this.player = player;
            king = false;
        }

        private static final Piece NULL = new Piece();

        public Piece copy() {
            if (isNull())
                return this;

            return new Piece(this.player, this.king);
        }

        public void makeKing() {
            if (isNull())
                throw new RuntimeException("cannot king a blank space");

            this.king = true;
        }

        public boolean isNull() {
            return this.player.isEmpty();
        }

        public String toString(int index) {
            StringBuilder pieceString = new StringBuilder(background(index));
            if (isNull())
                return pieceString.append("   ").toString();

            String symbol = isKing() ? "X" : "O";
            if (this.player.equals("B"))
                return pieceString.append(" ").append(symbol).append(" ").toString();
            else
                return pieceString.append(" ").append(ConsoleColors.RED)
                    .append(symbol).append(ConsoleColors.BLACK).append(" ")
                    .toString();
        }

        private static String background(int index) {
            if (index % 2 == 0)
                return ConsoleColors.LIGHT_RED_BACKGROUND;
            else
                return ConsoleColors.GRAY_BACKGROUND;
        }
    }

    public record Move(int srcRow, int srcCol, int dstRow, int dstCol) {
        public boolean isSkip() {
            return Math.abs(dstRow - srcRow) == 2;
        }

        public boolean isValid() {
            return (
                (Math.abs(dstRow - srcRow) == 1 && Math.abs(dstCol - srcCol) == 1) ||
                    (Math.abs(dstRow - srcRow) == 2 && Math.abs(dstCol - srcCol) == 2)
            );
        }

        public boolean equals(Move m) {
            return (srcRow == m.srcRow) && (srcCol == m.srcCol) && (dstRow == m.dstRow) && (dstCol == m.dstCol);
        }
    }
}
