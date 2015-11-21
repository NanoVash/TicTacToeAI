package com.nanovash.tictactoeai.windows;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.Manager;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.managers.AIManager;
import com.nanovash.tictactoeai.managers.HumanManager;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIWindow extends JFrame {

    private JPanel contentPane = new JPanel(new BorderLayout());
    private JButton startGameButton = new JButton("Launch Game");
    private JPanel main = new JPanel();
    private @Getter JPanel left = createSidePanel(true);
    private @Getter JPanel right = createSidePanel(false);
    private @Getter JComboBox<String> leftBox;
    private @Getter JComboBox<String> rightBox;

    private final Manager[] managers = new Manager[]{new HumanManager(), new AIManager()};
    private int leftSelected = 0;
    private int rightSelected = managers.length > 1 ? 1 : 0;

    public UIWindow() {
        setTitle("Tic Tac Toe AI Launcher");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setBounds(200, 200, 500, 600);

        startGameButton.setFocusPainted(false);
        startGameButton.setFont(createFont(startGameButton, 18));
        startGameButton.addActionListener(e -> {
            new Thread(() -> {
                GameWindow window = new GameWindow();
                window.setVisible(true);
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        for (Manager manager : managers)
                            manager.exit();
                    }
                });
                Game game = new Game(window);
                game.init();
                game.setPlayers(new Player[]{managers[leftSelected].getChosen(game, left), managers[rightSelected].getChosen(game, right)});
                window.setGame(game);
                while(true) {
                    game.start();
                    game.clear();
                    if(game.isDone())
                        break;
                }
            }).start();
        });
        contentPane.add(startGameButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for(Manager manager : managers)
                    manager.exit();
            }
        });

        leftBox = createComboBox(false);
        left.add(leftBox, BorderLayout.NORTH);

        rightBox = createComboBox(true);
        right.add(rightBox, BorderLayout.NORTH);

        main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
        main.add(left);
        main.add(right);

        if(managers.length == 0)
            throw new RuntimeException("No player types were found!");

        JPanel lc = managers[0].getUIConfigurator(left, right);
        addPanel(left, lc);

        JPanel rc = managers[managers.length > 1 ? 1 : 0].getUIConfigurator(right, left);
        addPanel(right, rc);

        contentPane.add(main, BorderLayout.CENTER);
    }

    private JComboBox<String> createComboBox(boolean isRight) {
        String[] ps = new String[managers.length];
        for (int i = 0; i < managers.length; i++) {
            ps[i] = managers[i].getPlayerDisplayName();
        }
        JComboBox<String> temp = new JComboBox<>(ps); 
        ((JLabel) temp.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        temp.setFocusable(false);
        temp.setFont(createFont(temp, 20));
        temp.setBackground(Color.WHITE);
        if(isRight)
            temp.setSelectedIndex(managers.length > 1 ? 1 : 0);
        temp.addItemListener(e -> {
            try {
                UIWindow.class.getDeclaredField(isRight ? "rightSelected" : "leftSelected").setInt(UIWindow.this, temp.getSelectedIndex());
            } catch (IllegalAccessException | NoSuchFieldException e1) {
                e1.printStackTrace();
            }
            JPanel c = managers[((JComboBox<String>) e.getSource()).getSelectedIndex()].getUIConfigurator(isRight ? right : left, isRight ? left : right);
            Container parent = temp.getParent();
            Component layoutComponent = ((BorderLayout) parent.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if(layoutComponent != null)
                parent.remove(layoutComponent);
            if (c != null)
                parent.add(c, BorderLayout.CENTER);
            parent.repaint();
            parent.revalidate();
        });
        return temp;
    }

    private JPanel createSidePanel(boolean left) {
        JPanel temp = new JPanel();
        temp.setBackground(Color.WHITE);
        temp.setLayout(new BorderLayout());
        if(left)
            temp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
        else
            temp.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        return temp;
    }

    private void addPanel(JPanel side, JPanel c) {
        if(c != null) {
            side.add(c, BorderLayout.CENTER);
            side.repaint();
            side.revalidate();
        }
    }

    public static Font createFont(Component c, int i) {
        return new Font(c.getFont().getName(), Font.PLAIN, i);
    }
}