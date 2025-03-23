package cli;

import agents.Agent;
import games.games2d.ConnectFour;
import resources.Players;

import java.util.Scanner;

public class ConnectFourCLI extends CommandLineApp<Integer> {
    public ConnectFourCLI(Agent botAgent, Scanner scanner) {
        super(botAgent, 5, scanner, ConnectFour::new);
    }

    @Override
    protected Integer playerMoveChoice() {
        int move = -1;
        boolean valid;
        do {
            System.out.print("Choose a column: ");
            try {
                move = Integer.parseInt(scanner.nextLine()) - 1;
                valid = this.game.moveLegal(move);
            } catch (NumberFormatException e) {
                valid = false;
            }
        } while (!valid);

        return move;
    }
}
