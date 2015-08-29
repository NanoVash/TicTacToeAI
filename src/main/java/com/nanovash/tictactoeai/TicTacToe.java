package com.nanovash.tictactoeai;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class TicTacToe {

	static UIWindow window;

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> {
			window = new UIWindow();
			window.setVisible(true);
		});
		Game game = new Game(window);
		while(true) {
			game.init();
			game.start();
		}
	}
}
