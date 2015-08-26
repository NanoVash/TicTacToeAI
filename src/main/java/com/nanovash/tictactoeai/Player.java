package com.nanovash.tictactoeai;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Player {

	private Game game;
	private @Getter String symbol;

	public abstract Location startTurn();
}
