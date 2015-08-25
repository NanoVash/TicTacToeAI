package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Human extends Player {

	final Human human = this;
	Location clicked;
	boolean isTurn;

	public Human(Game game, String symbol) {
		super(game, symbol);
		for(Tile[] tiles: game.tiles) {
			for(Tile t : tiles) {
				t.getLabel().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (isTurn) {
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
		return clicked;
	}
}