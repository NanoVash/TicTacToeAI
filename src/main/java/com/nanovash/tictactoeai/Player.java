package com.nanovash.tictactoeai;

import lombok.*;

@RequiredArgsConstructor
public abstract class Player {

	private @NonNull @Getter Game game;
	private @Getter @Setter char symbol;
	private @Getter @Setter String name;

	public abstract Location startTurn();

	public abstract void setName();

	public abstract void startGame();

	public abstract void endGame();
}