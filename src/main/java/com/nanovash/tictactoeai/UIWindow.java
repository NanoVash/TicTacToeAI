package com.nanovash.tictactoeai;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.GridLayout;

public class UIWindow extends JFrame {

	GridLayout gl = new GridLayout(3, 3);
	JPanel contentPane = new JPanel(gl);

	public UIWindow() {
		setTitle("Tic Tac Toe AI");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		setBounds(200, 200, 600, 600);
	}
}

