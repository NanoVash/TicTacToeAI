package com.nanovash.tictactoeai;

import lombok.*;

@RequiredArgsConstructor
public abstract class Player {

	@NonNull private Game game;
	private @Getter @Setter String symbol;

	public abstract Location startTurn();
}
