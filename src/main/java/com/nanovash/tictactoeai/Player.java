package com.nanovash.tictactoeai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Player {

	private Game game;
	private @Getter @Setter String symbol;

	public abstract Location startTurn();
}
