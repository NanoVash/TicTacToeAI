package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.players.AI;
import com.nanovash.tictactoeai.players.Human;

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
		game.init();
		game.setPlayers(new Player[]{new Human(game), new AI(game)});
		while(true) {
			game.start();
			game.clear();
		}
	}
}
