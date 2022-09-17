package Games;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class GameBoard2D <MoveData> {

    protected static final Scanner scanner = new Scanner(System.in);

    public class Square {
        public final int row;
        public final int col;

        public Square(int r, int c) {
            row = r;
            col = c;
        }

        public boolean inBounds() {
            return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
        }

        public Square shift(int up, int right) {
            return new Square(row + up, col + right);
        }

        public boolean free() {
            return board[row][col] == 0;
        }

        public boolean equals(Square s) {
            return (row == s.row) && (col == s.col);
        }
    }

    public class Move {
        public MoveData info;

        public Move(MoveData x) {
            info = x;
        }
    }

    protected final int[][] board;

    public GameBoard2D(int rows, int cols) {
        board = new int[rows][cols];
    }

    public GameBoard2D(GameBoard2D b) {
        board = boardCopy(b.board);
    }

    public int get(Square square) {
        return board[square.row][square.col];
    }

    //---------- ABSTRACT CLASSES ----------//

    public abstract boolean gameOver();
    public abstract GameBoard2D move(int agent, Move move);
    public abstract List<Move> allMoves(int agent);
    public abstract Move playerInput();
    public abstract String visualize();
    public abstract int heuristic(int agent);
    public abstract boolean compareMoves(Move m1, Move m2);

    private static int[][] boardCopy(int[][] b) {
        int[][] newBoard = new int[b.length][b[0].length];
        for (int i = 0; i < b.length; i++) {
            newBoard[i] = Arrays.copyOf(b[i], b[i].length);
        }

        return newBoard;
    }
}
