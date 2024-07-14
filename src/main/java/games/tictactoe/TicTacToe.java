package games.tictactoe;

import games.Game2D;

import java.util.ArrayList;

public class TicTacToe extends Game2D<TTTSquare> {
    private ArrayList<ArrayList<TTTSquare>> board;

    public TicTacToe() {
        this.board = board(3, 3);
    }

    protected TTTSquare square(int r, int c) {
        return new TTTSquare(r, c);
    }
}
