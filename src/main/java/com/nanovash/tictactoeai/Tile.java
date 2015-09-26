package com.nanovash.tictactoeai;

import lombok.*;

import javax.swing.*;

@RequiredArgsConstructor
public class Tile {

	private @NonNull @Getter @Setter JLabel label;
	private @NonNull @Getter @Setter Location location;
	private @Getter Player owner;
	private @Getter int order;

	public void setOwner(Player p, int i) {
		label.setText(p == null ? "" : Character.toString(p.getSymbol()));
		owner = p;
		order = i;
	}
}