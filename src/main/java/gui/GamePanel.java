package gui;

import agents.*;
import games.Game2D;
import lombok.Getter;
import lombok.Setter;
import resources.MonteCarloTree;
import resources.PlayerTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class GamePanel<M> extends JPanel {
    private static final Random random = new Random();

    @Getter private final String title;
    @Getter @Setter private Runnable win;
    @Getter @Setter private Runnable lose;
    @Getter @Setter private Runnable draw;

    protected final int rows;
    protected final int cols;
    protected final PlayerTypes playerTypes;
    protected String player;
    protected boolean playerGoesFirst;
    protected List<List<JButton>> spaces;
    protected Game2D<?, M> game;
    protected MonteCarloTree<M> monteCarloTree;
    protected Agent botAgent;

    public GamePanel(String title, PlayerTypes playerTypes, int rows, int cols, int depth) {
        super();
        this.title = title;
        this.rows = rows;
        this.cols = cols;
        this.playerTypes = playerTypes;
        this.player = playerChoice();
        this.playerGoesFirst = random.nextBoolean();
        this.spaces = new ArrayList<>();
        this.botAgent = new Max();
        this.game = newGame();
        this.monteCarloTree = new MonteCarloTree<>(this.game, this.playerTypes.opposite(this.player), depth);
        setup();
    }

    private void setup() {
        this.setLayout(new GridLayout(rows, cols, 5, 5));
        this.setBounds(125, 100, 750, 750);

        for (int r = 0; r < rows; r++) {
            spaces.add(new ArrayList<>());
            for (int c = 0; c < cols; c++) {
                JButton space = newSpace(r, c);
                space.addActionListener(this::actionListener);

                spaces.get(r).add(space);
                this.add(space);
            }
        }
    }

    public abstract void actionListener(ActionEvent e);
    public abstract GamePanel<M> copy();
    protected abstract String playerChoice();
    protected abstract Game2D<?, M> newGame();
    protected abstract JButton newSpace(int r, int c);
}
