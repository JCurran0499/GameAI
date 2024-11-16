package gui.connect4;

import games.Game2D;
import games.games2d.ConnectFour;
import gui.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ConnectFourPanel extends GamePanel<Integer> {
    private final int[] openRows;

    public ConnectFourPanel() {
        super("Connect Four", 6, 7);
        this.openRows = new int[] {5, 5, 5, 5, 5, 5, 5};
        if (!game.activeAgent().equals(playerAgent))
            botMove();
    }

    @Override
    public void actionListener(ActionEvent e) {
        ConnectFourSpace space = ((ConnectFourSpace) e.getSource());
        playerMove(space);

        if (!game.gameOver()) {
            // Bot takes its turn
            botMove();
        }

        if (game.gameOver()) {
            for (List<JButton> row : this.spaces)
                for (JButton button : row)
                    button.setEnabled(false);

            if (game.winner() == null) {
                this.getDraw().run();
            } else {
                if (game.winner().equals(this.playerAgent))
                    this.getWin().run();
                else
                    this.getLose().run();
            }
        }
    }

    @Override
    public ConnectFourPanel copy() {
        return new ConnectFourPanel();
    }

    @Override
    protected String playerAgentChoice() {
        return "B";
    }

    @Override
    protected Game2D<?, Integer> newGame() {
        return new ConnectFour(this.playerAgent, this.playerGoesFirst);
    }

    @Override
    protected JButton newSpace(int r, int c) {
        ConnectFourSpace space = new ConnectFourSpace(c);
        if (r < 5)
            space.setEnabled(false);
        return space;
    }

    private void playerMove(ConnectFourSpace space) {
        space.setPlayer(this.game.activeAgent());
        Integer playerMove = space.getColumn();
        this.game = this.game.move(playerMove);
        this.monteCarloTree.move(playerMove);

        openRows[playerMove] = openRows[playerMove] - 1;
        if (openRows[playerMove] >= 0) {
            this.spaces.get(openRows[playerMove]).get(playerMove).setEnabled(true);
        }
    }

    private void botMove() {
        Integer botMove = this.botAgent.takeTurn(this.monteCarloTree);
        ConnectFourSpace space = ((ConnectFourSpace) this.spaces.get(openRows[botMove]).get(botMove));
        space.setPlayer(this.game.activeAgent());
        this.game = this.game.move(botMove);

        openRows[botMove] = openRows[botMove] - 1;
        if (openRows[botMove] >= 0) {
            this.spaces.get(openRows[botMove]).get(botMove).setEnabled(true);
        }
    }
}
