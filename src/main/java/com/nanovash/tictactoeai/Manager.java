package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.windows.*;

import javax.swing.*;


public interface Manager {

    /**
     * Gets the UIConfigureer of this manager's player
     * @return The {@link JPanel} to be displayed in the {@link UIWindow} when the player selects this manager's player
     */
    JPanel getUIConfigurator(JPanel panel, JPanel other);

    /**
     * Called when the {@link UIWindow} and each of the {@link GameWindow}s are closed
     */
    void exit();

    /**
     * Called when the Start Game button is clicked
     * @param game The game
     * @return Get the selected player instance
     */
    Player getChosen(Game game, JPanel panel);

    /**
     * Gets the display name of this manager's player
     * @return The player's name to be displayed in the {@link UIWindow}
     */
    String getPlayerDisplayName();
}