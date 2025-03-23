package gui.connect4;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class ConnectFourSpace extends JButton {
    private static final ImageIcon imageR = new ImageIcon("src/main/resources/icons/red_chip.png");
    private static final ImageIcon imageB = new ImageIcon("src/main/resources/icons/blue_chip.png");

    private final int column;

    public ConnectFourSpace(int column) {
        super();
        this.column = column;
        setup();
    }

    private void setup() {
        this.setFocusable(false);

        int height = this.getPreferredSize().height * 12;
        int width = this.getPreferredSize().width * 4;
        Image imageRFixed = imageR.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image imageBFixed = imageB.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageR.setImage(imageRFixed);
        imageB.setImage(imageBFixed);
    }

    public void setPlayer(String player) {
        if (player.equals("R")) {
            this.setIcon(imageR);
            this.setDisabledIcon(imageR);
        } else {
            this.setIcon(imageB);
            this.setDisabledIcon(imageB);
        }

        this.setEnabled(false);
    }
}
