package gui;

import agents.*;
import games.Game2D;
import lombok.Getter;
import lombok.Setter;
import resources.MonteCarloTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public abstract class GamePanel<M> extends JPanel {
    private static final Random random = new Random();

    @Getter private final String title;
    @Getter @Setter private Runnable win;
    @Getter @Setter private Runnable lose;
    @Getter @Setter private Runnable draw;

    protected final int rows;
    protected final int cols;
    protected String player;
    protected List<List<JButton>> spaces;
    protected Game2D<?, M> game;
    protected MonteCarloTree<M> monteCarloTree;
    protected Agent botAgent;

    public GamePanel(String title, int rows, int cols, int depth, Callable<Game2D<?, M>> f) {
        super();

        try {
            this.game = f.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.title = title;
        this.rows = rows;
        this.cols = cols;
        this.spaces = new ArrayList<>();
        this.botAgent = new Max();

        boolean playerGoesFirst = random.nextBoolean();
        if (playerGoesFirst)
            this.player = this.game.getPlayers().player1();
        else
            this.player = this.game.getPlayers().player2();

        this.monteCarloTree = new MonteCarloTree<>(this.game, this.game.getPlayers().opposite(this.player), depth);
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
    protected abstract JButton newSpace(int r, int c);
}
