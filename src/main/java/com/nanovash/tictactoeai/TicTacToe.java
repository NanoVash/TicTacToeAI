package com.nanovash.tictactoeai;

import javax.swing.SwingUtilities;

public class TicTacToe {

	static UIWindow window;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			window = new UIWindow();
			window.setVisible(true);
		});
		while(true) {
			try {
				Thread.sleep(1);
				if(window != null) {
					new Game(window);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
