package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.*;

import java.util.Random;

public class AI extends Player {

	public AI(Game game) {
		super(game);
	}

	@Override
	public Location startTurn() {
		Random r = new Random();
		return new Location(r.nextInt(3), r.nextInt(3));
	}

	@Override
	public String getCustomName() {
		return "AI";
	}
}
