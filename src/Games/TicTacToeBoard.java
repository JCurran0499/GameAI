package Games;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeBoard extends GameBoard2D<GameBoard2D.Square> {
    private Square lastPlaced;

    public TicTacToeBoard() {
        super(3, 3);
        lastPlaced = null;
    }

    public TicTacToeBoard(TicTacToeBoard b) {
        super(b);
        lastPlaced = b.lastPlaced;
    }

    public boolean gameOver() {
        if (lastPlaced == null)
            return false;

        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][0] == board[i][2])
                return true;
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[0][i] == board[2][i])
                return true;
        }

        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2])
            return true;
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[0][2] == board[2][0])
            return true;

        return false;
    }

    public TicTacToeBoard move(int agent, Move move) {
        TicTacToeBoard newBoard = new TicTacToeBoard(this);
        newBoard.board[move.info.row][move.info.col] = agent;
        newBoard.lastPlaced = move.info;

        return newBoard;
    }

    public List<Move> allMoves(int agent) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (board[r][c] == 0) {
                    Move m = new Move(new Square(r, c));
                    moves.add(m);
                }

        return moves;
    }

    public Move playerInput() {
        Square choice = null;
        while (!(choice != null && choice.inBounds() && choice.free())) {
            System.out.print("Square (1-9): ");
            int squareChoice = scanner.nextInt() - 1;
            choice = new Square(squareChoice / 3, squareChoice % 3);
        }

        return new Move(choice);
    }

    public String visualize() {
        StringBuilder v = new StringBuilder();
        v.append(" -".repeat(3));
        v.append("\n");

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                v.append("|");

                if (board[r][c] == 1)
                    v.append("X");
                else if (board[r][c] == -1)
                    v.append("O");
                else
                    v.append((r * 3) + c + 1);
            }
            v.append("|\n");
        }

        v.append(" -".repeat(3));
        v.append("\n");

        return v.toString();
    }

    public int heuristic(int agent) {
        return gameOver() ? (agent * -1) : 0;
    }

    public boolean compareMoves(Move m1, Move m2) {
        return m1.info.equals(m2.info);
    }
}
