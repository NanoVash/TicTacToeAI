package com.nanovash.tictactoeai;

import lombok.*;

@RequiredArgsConstructor
public abstract class Player {

	private @NonNull @Getter Game game;
	private @Getter @Setter String symbol;
	private @Getter @Setter String name;

	public abstract Location startTurn();

	public abstract String getCustomName();
}
