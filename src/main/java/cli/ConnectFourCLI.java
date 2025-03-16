package cli;

import agents.Agent;
import games.games2d.ConnectFour;
import resources.PlayerTypes;

import java.util.Scanner;

public class ConnectFourCLI extends CommandLineApp<Integer> {
    public ConnectFourCLI(Agent botAgent, Scanner scanner) {
        super(botAgent, PlayerTypes.CONNECT_FOUR, 5, scanner);
    }

    @Override
    protected ConnectFour newGame() {
        if (this.playerGoesFirst)
            return new ConnectFour(this.player);
        else
            return new ConnectFour(this.playerTypes.opposite(this.player));
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
