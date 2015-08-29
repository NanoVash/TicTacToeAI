package com.nanovash.tictactoeai;

import lombok.*;

@RequiredArgsConstructor
public abstract class Player {

	private @NonNull Game game;
	private @Getter @Setter String symbol;

	public abstract Location startTurn();
}
