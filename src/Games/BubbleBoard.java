package Games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleBoard extends GameBoard2D<List<GameBoard2D.Square>> {
    private final int[] openPerRow;
    private final int size;

    public BubbleBoard(int s) {
        super(s, s);
        openPerRow = new int[s];
        size = s;

        for (int i = 0; i < s; i++) {
            openPerRow[i] = s;
            Arrays.fill(board[i], 0);
        }
    }

    public BubbleBoard(BubbleBoard b) {
        super(b);
        openPerRow = Arrays.copyOf(b.openPerRow, b.openPerRow.length);
        size = b.size;
    }

    public boolean gameOver() {
        int sum = 0;
        for (int openSpots : openPerRow)
            sum += openSpots;

        return sum < 2;
    }

    public BubbleBoard move(int agent, Move move) { //assumes all positions are valid
        BubbleBoard newBoard = new BubbleBoard(this);
        for (Square s : move.info) {
            newBoard.board[s.row][s.col] = agent;
            newBoard.openPerRow[s.row]--;
        }

        return newBoard;
    }

    public List<Move> allMoves(int agent) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<GameBoard2D.Square> squares;
        Square pointer;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                squares = new ArrayList<>();
                pointer = new Square(r, c);

                while (pointer.inBounds() && pointer.free()) {
                    squares.add(pointer);
                    moves.add(new Move(squares));
                    squares = (ArrayList<GameBoard2D.Square>) squares.clone();

                    pointer = pointer.shift(0, 1);
                }
            }
        }

        return moves;
    }

    public Move playerInput() {
        ArrayList<GameBoard2D.Square> squares = new ArrayList<>();
        Square s;

        boolean invalidSequence = true;
        while (invalidSequence) {

            int row = -1, start = -1, end = -1;
            while (!(row >= 0 && row < size)) {
                System.out.print("Row (1-" + size + "): ");
                row = scanner.nextInt() - 1;
            }
            while (!(start >= 0 && start < size)) {
                System.out.print("Start (1-" + size + "): ");
                start = scanner.nextInt() - 1;
            }
            while (!(end >= start && row < size)) {
                System.out.print("End (" + (start + 1) + "-" + size + "): ");
                end = scanner.nextInt() - 1;
            }

            for (int i = start; i <= end; i++) {
                s = new Square(row, i);
                if (!s.free()) {
                    System.out.println("invalid sequence...");
                    squares.clear();
                    invalidSequence = true;
                    break;
                }
                else {
                    squares.add(s);
                    invalidSequence = false;
                }
            }
        }

        return new Move(squares);
    }

    public String visualize() {
        StringBuilder v = new StringBuilder();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {

                if (board[r][c] == 0)
                    v.append("o ");
                else
                    v.append("x ");
            }
            v.append("\n");
        }

        return v.toString();
    }

    public int heuristic(int agent) {
        return gameOver() ? (agent * -1) : 0;
    }

    public boolean compareMoves(Move m1, Move m2) {
        if (m1.info.size() != m2.info.size())
            return false;

        for (int i = 0; i < m1.info.size(); i++)
            if (!m1.info.get(i).equals(m2.info.get(i)))
                return false;

        return true;
    }
}
