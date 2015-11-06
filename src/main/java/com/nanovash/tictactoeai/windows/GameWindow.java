package com.nanovash.tictactoeai.windows;

import com.nanovash.tictactoeai.Game;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

	private GridLayout gl = new GridLayout(3, 3);
	private @Getter JPanel contentPane = new JPanel(gl);
	private @Setter Game game;

	public GameWindow() {
		setTitle("Tic Tac Toe AI");
		setContentPane(contentPane);
		setBounds(700, 200, 600, 600);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.setDone(true);
                e.getWindow().dispose();
            }
        });
	}
}