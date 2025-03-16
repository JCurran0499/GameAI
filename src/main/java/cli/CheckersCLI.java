package cli;

import agents.Agent;
import games.games2d.Checkers;
import resources.PlayerTypes;

import java.util.Scanner;
import java.util.Set;

public class CheckersCLI extends CommandLineApp<Checkers.Move> {
    private static final Set<Character> columns = Set.of(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'
    );

    public CheckersCLI(Agent botAgent, Scanner scanner) {
        super(botAgent, PlayerTypes.CHECKERS, 3, scanner);
    }

    @Override
    protected Checkers newGame() {
        if (this.playerGoesFirst)
            return new Checkers(this.player);
        else
            return new Checkers(this.playerTypes.opposite(this.player));
    }

    @Override
    protected Checkers.Move playerMoveChoice() {
        Checkers.Move move = null;
        boolean valid = false;
        do {
            System.out.print("Enter your start and end space: ");
            String line = scanner.nextLine();
            String[] spaces = line.split(" ");
            if (spaces.length == 2) {
                String choice1 = spaces[0].strip().toUpperCase();
                String choice2 = spaces[1].strip().toUpperCase();

                if (validChoice(choice1) && validChoice(choice2)) {
                    int srcCol = choice1.charAt(0) - 'A';
                    int srcRow = choice1.charAt(1) - '1';

                    int dstCol = choice2.charAt(0) - 'A';
                    int dstRow = choice2.charAt(1) - '1';

                    move = new Checkers.Move(srcRow, srcCol, dstRow, dstCol);
                    valid = game.moveLegal(move);
                }
            }
        } while (!valid);

        return move;
    }

    private boolean validChoice(String choice) {
        if (choice.length() != 2)
            return false;

        char col = choice.charAt(0);
        char row = choice.charAt(1);

        return Character.isLetter(col) && Character.isDigit(row);
    }
}
