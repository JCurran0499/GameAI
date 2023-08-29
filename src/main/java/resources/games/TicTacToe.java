package resources.games;

import resources.GameBoard;
import lombok.Getter;

public class TicTacToe implements GameBoard<TicTacToe.Move> {
    int[][] board;
    int turn = 1;

    public TicTacToe() {
        board = new int[][] {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
    }

    public int playerCount() {
        return 2;
    }

    public int playerTurn() {
        return turn;
    }

    public boolean gameOver() {
        return false;
    }

    public int winner() {
        return 0;
    }

    public Move[] validMoves() {
        return null;
    }

    public Move lastMove() {
        return null;
    }

    public String string() {
        return "";
    }

    public TicTacToe move(Move m) {
        return null;
    }

    @Getter
    public class Move {
        private final int row;
        private final int col;

        public Move(int r, int c) {
            row = r;
            col = c;
        }
    }
}
