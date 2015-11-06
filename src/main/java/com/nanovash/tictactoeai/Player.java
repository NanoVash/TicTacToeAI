package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.util.Location;
import lombok.Getter;
import lombok.Setter;

public abstract class Player {

	private @Getter @Setter String name;

    /**
     * Called when the player's turn starts
     * @param game The game
     * @return Where the player chose to mark
     */
	public abstract Location startTurn(Game game);

    /**
     * Called when the Player should be prompted to input a name
     * @param game The game
     */
	public abstract void changeName(Game game);

    /**
     * Called when the game starts
     * @param game The game
     */
	public abstract void startGame(Game game);

    /**
     * Called when the game ends
     * @param game The game
     */
	public abstract void endGame(Game game);
}