package gui.tictactoe;

import games.Game2D;
import games.games2d.TicTacToe;
import gui.GamePanel;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class TicTacToePanel extends GamePanel<TicTacToe.Move> {
    public TicTacToePanel() {
        super("Tic Tac Toe", 3, 3);
        if (!game.activeAgent().equals(playerAgent))
            botMove();
    }

    @Override
    public void actionListener(ActionEvent e) {
        if (e.getSource().getClass() == TicTacToeSpace.class) {
            TicTacToeSpace space = ((TicTacToeSpace) e.getSource());
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
    }

    @Override
    public TicTacToePanel copy() {
        return new TicTacToePanel();
    }

    @Override
    protected String playerAgentChoice() {
        return "O";
    }

    @Override
    protected Game2D<?, TicTacToe.Move> newGame() {
        return new TicTacToe(this.playerAgent, this.playerGoesFirst);
    }

    @Override
    protected JButton newSpace(int r, int c) {
        return new TicTacToeSpace(r, c);
    }

    private void playerMove(TicTacToeSpace space) {
        space.setPlayer(this.game.activeAgent());
        TicTacToe.Move playerMove = new TicTacToe.Move(space.getRow(), space.getCol());
        this.game = this.game.move(playerMove);
        this.monteCarloTree.move(playerMove);
    }

    private void botMove() {
        TicTacToe.Move botMove = this.botAgent.takeTurn(this.monteCarloTree);
        TicTacToeSpace space2 = ((TicTacToeSpace) this.spaces.get(botMove.x()).get(botMove.y()));
        space2.setPlayer(this.game.activeAgent());
        this.game = this.game.move(botMove);
    }
}
