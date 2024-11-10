package gui;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static final Color GAMEPLAY_COLOR = new Color(150, 200, 250);
    private static final Color WIN_COLOR = new Color(130, 250, 150);
    private static final Color LOSE_COLOR = new Color(250, 140, 140);
    private static final Color DRAW_COLOR = new Color(180, 180, 180);

    private final JLabel titleLabel;
    private GamePanel<?> panel;

    public GameFrame(String title) {
        super(title);
        this.titleLabel = new JLabel("AI Games");
        this.panel = null;
        setup();
    }

    public void addPanel(GamePanel<?> panel) {
        if (this.panel != null)
            this.remove(this.panel);

        panel.setWin(this::win);
        panel.setDraw(this::draw);
        panel.setLose(this::lose);
        this.panel = panel;
        titleLabel.setText(panel.getTitle());
        this.add(panel);
        this.setVisible(true);
    }

    private void setup() {
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.getContentPane().setBackground(GAMEPLAY_COLOR);

        ImageIcon image = new ImageIcon("src/main/resources/icons/x.png");
        this.setIconImage(image.getImage());

        titleLabel.setBounds(125, 0, 750, 100);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
        this.add(titleLabel);

        JButton resetButton = new JButton("New Game");
        resetButton.setBounds(225, 860, 550, 80);
        resetButton.addActionListener(e -> {
            this.getContentPane().setBackground(GAMEPLAY_COLOR);
            this.addPanel(this.panel.copy());
        });
        this.add(resetButton);
    }

    private void win() {
        this.getContentPane().setBackground(WIN_COLOR);
    }

    private void lose() {
        this.getContentPane().setBackground(LOSE_COLOR);
    }

    private void draw() {
        this.getContentPane().setBackground(DRAW_COLOR);
    }
}
