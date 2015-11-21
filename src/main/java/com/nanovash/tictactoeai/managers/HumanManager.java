package com.nanovash.tictactoeai.managers;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.Manager;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.players.Human;

import javax.swing.*;

public class HumanManager implements Manager {

    @Override
    public JPanel getUIConfigurator(JPanel panel, JPanel other) {
        return null;
    }

    @Override
    public void exit() {}

    @Override
    public Player getChosen(Game game, JPanel panel) {
        return new Human(game);
    }

    @Override
    public String getPlayerDisplayName() {
        return "Human";
    }
}