package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Human extends Player {

	final Human human = this;
	Location clicked;
	boolean isTurn;

	private static int playerNum = 1;

	public Human(Game game) {
		super(game);
		for(Tile[] tiles: game.tiles) {
			for(Tile t : tiles) {
				t.getLabel().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (isTurn && t.getOwner() == null) {
							synchronized (human) {
								clicked = t.getLocation();
								human.notify();
							}
						}
						System.out.println("clicked");
					}
				});
			}
		}
	}

	@Override
	public Location startTurn() {
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
	public String getCustomName() {
		String name;
		do {
			name = getName() == null ? JOptionPane.showInputDialog(getGame().getWindow(), "Input your name, Player " + playerNum + ".", "Choose a name.", JOptionPane.PLAIN_MESSAGE) : getName();
		} while(name == null || name.trim().equals(""));
		playerNum++;
		return name;
	}

	public static void reset() {
		playerNum = 1;
	}
}
