package gui.tictactoe;

import lombok.Getter;
import javax.swing.*;
import java.awt.*;

@Getter
public class TicTacToeSpace extends JButton {
    private static final ImageIcon imageX = new ImageIcon("src/main/resources/icons/x.png");
    private static final ImageIcon imageO = new ImageIcon("src/main/resources/icons/blue_o.png");

    private final int row;
    private final int col;

    public TicTacToeSpace(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        setup();
    }

    private void setup() {
        this.setFocusable(false);

        int height = this.getPreferredSize().height * 15;
        int width = this.getPreferredSize().width * 5;
        Image imageXFixed = imageX.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image imageOFixed = imageO.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageX.setImage(imageXFixed);
        imageO.setImage(imageOFixed);
    }

    public void setPlayer(String player) {
        if (player.equals("X")) {
            this.setIcon(imageX);
            this.setDisabledIcon(imageX);
        } else {
            this.setIcon(imageO);
            this.setDisabledIcon(imageO);
        }

        this.setEnabled(false);
    }
}
