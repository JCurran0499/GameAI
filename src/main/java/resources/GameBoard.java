package resources;

public interface GameBoard <Move> {
    int playerCount();
    int playerTurn();
    boolean gameOver();
    int winner();
    Move[] validMoves();
    Move lastMove();
    String string();
    GameBoard move(Move move);
}
