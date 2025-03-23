package cli;

import agents.Agent;
import games.games2d.TicTacToe;
import java.util.Scanner;

public class TicTacToeCLI extends CommandLineApp<TicTacToe.Move> {
    public TicTacToeCLI(Agent botAgent, Scanner scanner) {
        super(botAgent, 5, scanner, TicTacToe::new);
    }

    @Override
    protected TicTacToe.Move playerMoveChoice() {
        TicTacToe.Move move = null;
        boolean valid;
        do {
            System.out.print("Choose a board space: ");
            try {
                int space = Integer.parseInt(scanner.nextLine());
                move = new TicTacToe.Move((space - 1) / 3, (space - 1) % 3);
                valid = game.moveLegal(move);
            } catch (NumberFormatException e) {
                valid = false;
            }
        } while (!valid);

        return move;
    }
}
