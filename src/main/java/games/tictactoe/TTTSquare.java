package games.tictactoe;

import lombok.*;

@Getter
@Setter
public class TTTSquare {
    private final int row;
    private final int col;
    private int agent;

    public TTTSquare(int row, int col) {
        this.row = row;
        this.col = col;
        this.agent = 0;
    }
}
