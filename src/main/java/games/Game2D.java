package games;

import java.util.ArrayList;

public abstract class Game2D<S> {
    protected ArrayList<ArrayList<S>> board(int rows, int cols) {
        ArrayList<ArrayList<S>> board = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            board.add(new ArrayList<>());

            for (int c = 0; c < cols; c++)
                board.get(r).add(this.square(r, c));
        }

        return board;
    }

    protected abstract S square(int r, int c);
}
