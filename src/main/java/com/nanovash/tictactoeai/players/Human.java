package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.util.Location;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.util.Tile;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Human extends Player {

	private final Human human = this;
	private Location clicked;
	private boolean isTurn;

	private static int playerNum = 1;

	public Human(Game game) {
		for (Tile[] tiles : game.getTiles()) {
			for (Tile t : tiles) {
				t.getLabel().addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (isTurn && t.getOwner() == null) {
							synchronized (human) {
								clicked = t.getLocation();
								human.notify();
							}
						}
						System.out.println("clicked " + t.getLocation());
					}
				});
			}
		}
	}

	@Override
	public Location startTurn(Game game) {
		isTurn = true;
		try {
			synchronized (this) {
				wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isTurn = false;
		return clicked;
	}

	@Override
	public void changeName(Game game) {
		String name;
		do {
			name = getName() == null ? JOptionPane.showInputDialog(game.getWindow(), "Input your name, Player " + playerNum + ".", "Choose a name", JOptionPane.PLAIN_MESSAGE) : getName();
		} while (name == null || name.trim().equals(""));
		setName(name);
		playerNum++;
	}

    @Override
	public void startGame(Game game) {
        playerNum = 1;
    }

	@Override
	public void endGame(Game game) {}
}