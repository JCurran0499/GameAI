import agents.Agent;
import agents.*;
import cli.CommandLineApp;
import cli.TicTacToeCLI;
import resources.ConsoleColors;

public class App {
    public static void main(String[] args) {
        Agent agent = chooseAgent();
        CommandLineApp<?> app = chooseGame(agent);
        app.run();
    }

    private static Agent chooseAgent() {
        int agent;
        boolean valid;
        do {
            System.out.println("\n(1) Walker");
            System.out.println("(2) Max");
            System.out.print("Choose your agent: ");
            try {
                agent = Integer.parseInt(System.console().readLine().strip());
                valid = (agent > 0 && agent <= 2);
            } catch (NumberFormatException | NullPointerException e) {
                agent = -1;
                valid = false;
            }
        } while (!valid);

        return switch (agent) {
            case 1 -> new Walker();
            case 2 -> new Max();
            default -> null;
        };
    }

    private static CommandLineApp<?> chooseGame(Agent agent) {
        int game;
        boolean valid;
        do {
            System.out.println("\n(1) Tic Tac Toe");
            System.out.print("Choose your game: ");
            try {
                game = Integer.parseInt(System.console().readLine().strip());
                valid = (game == 1);
            } catch (NumberFormatException | NullPointerException e) {
                valid = false;
            }
        } while (!valid);

        return new TicTacToeCLI(agent);
    }
}
