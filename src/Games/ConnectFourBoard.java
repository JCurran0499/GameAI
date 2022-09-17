package Games;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourBoard extends GameBoard2D<Integer> {
    private final int rowDim;
    private final int colDim;
    private final int[] openRowPerCol;
    private Square lastPlaced;

    public ConnectFourBoard(int rows, int cols) {
        super(rows, cols);
        rowDim = rows;
        colDim = cols;
        openRowPerCol = new int[cols];
        lastPlaced = null;

        Arrays.fill(openRowPerCol, rowDim - 1);
        for (int r = 0; r < rows; r++)
            Arrays.fill(board[r], 0);
    }

    public ConnectFourBoard(ConnectFourBoard b) {
        super(b);
        rowDim = b.rowDim;
        colDim = b.colDim;
        openRowPerCol = Arrays.copyOf(b.openRowPerCol, b.openRowPerCol.length);
        lastPlaced = b.lastPlaced;
    }

    public boolean gameOver() {
        if (lastPlaced == null)
            return false;

        int agent = get(lastPlaced);

        int pointerN = sequencePointer(lastPlaced, agent, 0, -1);
        int pointerS = sequencePointer(lastPlaced, agent, 0, 1);
        int pointerW = sequencePointer(lastPlaced, agent, -1, 0);
        int pointerE = sequencePointer(lastPlaced, agent, 1, 0);
        int pointerNE = sequencePointer(lastPlaced, agent, 1, -1);
        int pointerSW = sequencePointer(lastPlaced, agent, -1, 1);
        int pointerNW = sequencePointer(lastPlaced, agent, -1, -1);
        int pointerSE = sequencePointer(lastPlaced, agent, 1, 1);

        return (pointerN + pointerS >= 3) || (pointerW + pointerE >= 3) ||
                (pointerNE + pointerSW >= 3) || (pointerNW + pointerSE >= 3);
    }

    public ConnectFourBoard move(int agent, Move move) {
        ConnectFourBoard newBoard = new ConnectFourBoard(this);
        newBoard.board[openRowPerCol[move.info]][move.info] = agent;
        newBoard.openRowPerCol[move.info]--;
        newBoard.lastPlaced = this.new Square(openRowPerCol[move.info] ,move.info);

        return newBoard;
    }

    public List<Move> allMoves(int agent) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int c = 0; c < colDim; c++)
            if (openRowPerCol[c] >= 0) {
                Move m = this.new Move(c);
                moves.add(m);
            }

        return moves;
    }

    public Move playerInput() {
        int choice = -1;
        while (!(choice >= 0 && choice < colDim && openRowPerCol[choice] >= 0)) {
            System.out.print("Column (1-" + colDim + "): ");
            choice = scanner.nextInt() - 1;
        }

        return new Move(choice);
    }

    public String visualize() {
        StringBuilder v = new StringBuilder();
        v.append(" -".repeat(colDim));
        v.append("\n");

        for (int r = 0; r < rowDim; r++) {
            for (int c = 0; c < colDim; c++) {
                v.append("|");

                if (board[r][c] == 1)
                    v.append("X");
                else if (board[r][c] == -1)
                    v.append("O");
                else
                    v.append(" ");
            }
            v.append("|\n");
        }

        v.append(" -".repeat(colDim));
        v.append("\n");
        for (int i = 0; i < colDim; i++)
            v.append(" ").append(i + 1);
        v.append("\n");

        return v.toString();
    }

    public int heuristic(int agent) {
        return gameOver() ? (agent * -1) : 0;
    }

    public boolean compareMoves(Move m1, Move m2) {
        return m1.info.equals(m2.info);
    }


    private int sequencePointer(Square square, int agent, int rowInterval, int colInterval) {
        int counter = -1;
        Square pointer = square;
        while (pointer.inBounds() && get(pointer) == agent) {
            counter++;
            pointer = pointer.shift(rowInterval, colInterval);
        }

        return counter;
    }
}
