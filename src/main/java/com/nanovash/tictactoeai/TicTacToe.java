package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.players.Human;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class TicTacToe {

	public static UIWindow window;

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(() -> {
			window = new UIWindow();
			window.setVisible(true);
		});
		Game game = new Game(window);
		AIManager manager = new AIManager(game);
		game.init();
		game.setPlayers(new Player[]{new Human(game), /*temporary*/ manager.getAI("AI")});
		while(true) {
			game.start();
			game.clear();
		}
	}
}