import cli.CommandLineApp;
import cli.TicTacToeCLI;
import games.tictactoe.TicTacToe;

public class App {
    public static void main(String[] args) {
        CommandLineApp<TicTacToe.Move> app = new TicTacToeCLI();
        app.run();
    }
}
