package cli;

import agents.Agent;
import games.games2d.TicTacToe;
import resources.PlayerTypes;
import java.util.Scanner;

public class TicTacToeCLI extends CommandLineApp<TicTacToe.Move> {
    public TicTacToeCLI(Agent botAgent, Scanner scanner) {
        super(botAgent, PlayerTypes.TIC_TAC_TOE, 5, scanner);
    }

    @Override
    protected TicTacToe newGame() {
        if (this.playerGoesFirst)
            return new TicTacToe(this.player);
        else
            return new TicTacToe(this.playerTypes.opposite(this.player));
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
